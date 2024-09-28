package com.kjs990114.goodong.application.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import com.kjs990114.goodong.adapter.out.persistence.entity.ModelEntity;
import com.kjs990114.goodong.adapter.out.persistence.entity.PostEntity;
import com.kjs990114.goodong.adapter.out.persistence.entity.UserEntity;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.adapter.out.persistence.repository.PostRepository;
import com.kjs990114.goodong.adapter.out.persistence.entity.ContributionEntity;
import com.kjs990114.goodong.adapter.out.persistence.repository.UserRepository;
import com.kjs990114.goodong.adapter.out.persistence.entity.PostDocument;
import com.kjs990114.goodong.adapter.in.web.dto.DTOMapper;
import com.kjs990114.goodong.adapter.in.web.dto.PostDTO;
import com.kjs990114.goodong.adapter.in.web.dto.RestPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final ElasticsearchOperations elasticsearchOperations;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String cacheName = "userPosts";
    @Value("${spring.page.size}")
    private int pageSize;

    @Transactional
    public void createPost(PostDTO.Create create, Long userId) throws IOException {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        ContributionEntity contributionEntity = new ContributionEntity();
        contributionEntity.setUser(userEntity);
        userEntity.updateContribution(contributionEntity);

        PostEntity newPostEntity = PostEntity.builder()
                .title(create.getTitle())
                .content(create.getContent())
                .status(create.getStatus())
                .user(userEntity)
                .build();

        String fileName = fileService.saveFileStorage(create.getFile(), FileService.Extension.GLB);
        ModelEntity newModelEntity = ModelEntity.builder()
                .commitMessage("First Commit")
                .post(newPostEntity)
                .fileName(fileName)
                .version(1)
                .build();

        newPostEntity.addModel(newModelEntity);
        newPostEntity.addTagAll(create.getTags());
        userEntity.posting(newPostEntity);
        userRepository.save(userEntity);
        postRepository.save(newPostEntity);
        redisTemplate.delete(cacheName + ":" + userId);
    }

    @Transactional(readOnly = true)
    public Boolean checkDuplicatedTitle(String title, Long userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exist"));
        return userEntity.getPosts().stream()
                .anyMatch(post -> post.getTitle().equalsIgnoreCase(title));
    }

    @Transactional(readOnly = true)
    public List<PostDTO.Summary> getMyPosts(Long userId) {
        List<PostEntity> postEntity = postRepository.findUserPostsAll(userId);
        return postEntity.stream().map(DTOMapper::postToSummary).toList();
    }

    @Transactional(readOnly = true)
    public PostDTO.PostInfo getPost(String fileName) {
        PostEntity postEntity = postRepository.findPostIdByFileName(fileName).orElseThrow(() -> new NotFoundException("Model does not exist"));
        return new PostDTO.PostInfo(postEntity.getStatus(), postEntity.getUser().getUserId());
    }

    @Transactional(readOnly = true)
    public RestPage<PostDTO.Summary> getPosts(Long userId, int page, boolean isMyPosts) {
        HashOperations<String, String, RestPage<PostDTO.Summary>> hashOp = redisTemplate.opsForHash();
        String key = cacheName + ":" + userId;
        String hashKey = page + ":" + isMyPosts;
        RestPage<PostDTO.Summary> cachedPage = hashOp.get(key, hashKey);
        if (cachedPage != null) {
            return cachedPage;
        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("lastModifiedAt").descending());
        Page<PostEntity> entityPage = isMyPosts ? postRepository.findUserPublicAndPrivatePosts(userId, pageable) : postRepository.findUserPublicPosts(userId, pageable);
        RestPage<PostDTO.Summary> dbPage = new RestPage<>(entityPage.map(DTOMapper::postToSummary));
        hashOp.put(key,hashKey,dbPage);
        return dbPage;
    }

    @Transactional(readOnly = true)
    public PostDTO.PostDetail getPost(Long postId) {
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("User does not exists"));
        return DTOMapper.postToDetail(postEntity);
    }

    @Transactional(readOnly = true)
    public Page<PostDTO.Summary> searchPosts(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, pageSize);

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(query -> query
                        .bool(bool -> bool
                                .should(should -> should.match(match -> match.field("title").query(FieldValue.of(keyword)).boost(3.0f)))
                                .should(should -> should.match(match -> match.field("tagging").query(FieldValue.of(keyword)).boost(2.0f)))
                                .should(should -> should.match(match -> match.field("content").query(FieldValue.of(keyword)).boost(1.0f)))
                        )
                )
                .withPageable(pageable)
                .build();

        SearchHits<PostDocument> searchHits = elasticsearchOperations.search(searchQuery, PostDocument.class);
        List<Long> postIdList = searchHits.stream().map(hit -> hit.getContent().getPostId()).toList();
        List<PostEntity> postEntityList = postRepository.findAllById(postIdList);
        List<PostDTO.Summary> summaries = postEntityList.stream()
                .filter(post -> post.getStatus().equals(PostEntity.PostStatus.PUBLIC))
                .map(DTOMapper::postToSummary)
                .toList();

        return new PageImpl<>(summaries, pageable, searchHits.getTotalHits());
    }

    @Transactional
    public void deletePost(Long postId) {
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        UserEntity userEntity = postEntity.getUser();
        userEntity.unposting(postId);
        postEntity.softDelete();
        userRepository.save(userEntity);
        postRepository.save(postEntity);
        redisTemplate.delete(cacheName + ":" + userEntity.getUserId());
    }

    @Transactional
    public void updatePost(Long postId, PostDTO.Update update) throws IOException {
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        UserEntity userEntity = postEntity.getUser();
        postEntity.updatePost(update.getTitle(), update.getContent());
        if (update.getTags() != null) {
            postEntity.removeTagAll();
            postEntity.addTagAll(update.getTags());
        }
        postEntity.updateStatus(update.getStatus());
        String commitMessage = update.getCommitMessage() == null || update.getCommitMessage().isEmpty() ? "Commit" : update.getCommitMessage();
        if (update.getFile() != null) {
            int nextVersion = postEntity.getNextModelVersion();
            String newFileName = fileService.saveFileStorage(update.getFile(), FileService.Extension.GLB);
            ModelEntity newModelEntity = ModelEntity.builder()
                    .post(postEntity)
                    .version(nextVersion)
                    .fileName(newFileName)
                    .commitMessage(commitMessage)
                    .build();
            postEntity.addModel(newModelEntity);
        }
        ContributionEntity contributionEntity = new ContributionEntity();
        contributionEntity.setUser(userEntity);
        userEntity.updateContribution(contributionEntity);
        postEntity.updateModifiedAt();
        postRepository.save(postEntity);
        userRepository.save(userEntity);
        redisTemplate.delete(cacheName + ":" + userEntity.getUserId());
    }

}
