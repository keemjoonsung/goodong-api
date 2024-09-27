package com.kjs990114.goodong.application.post;

import co.elastic.clients.elasticsearch._types.FieldValue;
import com.kjs990114.goodong.application.file.FileService;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.domain.post.*;
import com.kjs990114.goodong.domain.post.PostRepository;
import com.kjs990114.goodong.domain.user.Contribution;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.UserRepository;
import com.kjs990114.goodong.infrastructure.PostDocument;
import com.kjs990114.goodong.presentation.dto.DTOMapper;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import com.kjs990114.goodong.presentation.dto.RestPage;
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
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        Contribution contribution = new Contribution();
        contribution.setUser(user);
        user.updateContribution(contribution);

        Post newPost = Post.builder()
                .title(create.getTitle())
                .content(create.getContent())
                .status(create.getStatus())
                .user(user)
                .build();

        String fileName = fileService.saveFileStorage(create.getFile(), FileService.Extension.GLB);
        Model newModel = Model.builder()
                .commitMessage("First Commit")
                .post(newPost)
                .fileName(fileName)
                .version(1)
                .build();

        newPost.addModel(newModel);
        newPost.addTagAll(create.getTags());
        user.posting(newPost);
        userRepository.save(user);
        postRepository.save(newPost);
        redisTemplate.delete(cacheName + ":" + userId);
    }

    @Transactional(readOnly = true)
    public Boolean checkDuplicatedTitle(String title, Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exist"));
        return user.getPosts().stream()
                .anyMatch(post -> post.getTitle().equalsIgnoreCase(title));
    }

    @Transactional(readOnly = true)
    public List<PostDTO.Summary> getMyPosts(Long userId) {
        List<Post> post = postRepository.findUserPostsAll(userId);
        return post.stream().map(DTOMapper::postToSummary).toList();
    }

    @Transactional(readOnly = true)
    public PostDTO.PostInfo getPost(String fileName) {
        Post post = postRepository.findPostIdByFileName(fileName).orElseThrow(() -> new NotFoundException("Model does not exist"));
        return new PostDTO.PostInfo(post.getStatus(), post.getUser().getUserId());
    }

    @Transactional(readOnly = true)
    public RestPage<PostDTO.Summary> getPosts(Long userId, int page, boolean isMyPosts) {
//        HashOperations<String, String, RestPage<PostDTO.Summary>> hashOp = redisTemplate.opsForHash();
//        String key = cacheName + ":" + userId;
//        String hashKey = page + ":" + isMyPosts;
//        RestPage<PostDTO.Summary> cachedPage = hashOp.get(key, hashKey);
//        if (cachedPage != null) {
//            return cachedPage;
//        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("lastModifiedAt").descending());
        Page<Post> entityPage = isMyPosts ? postRepository.findUserPublicAndPrivatePosts(userId, pageable) : postRepository.findUserPublicPosts(userId, pageable);
        RestPage<PostDTO.Summary> dbPage = new RestPage<>(entityPage.map(DTOMapper::postToSummary));
//        hashOp.put(key,hashKey,dbPage);
        return dbPage;
    }

    @Transactional(readOnly = true)
    public PostDTO.PostDetail getPost(Long postId) {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("User does not exists"));
        return DTOMapper.postToDetail(post);
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
        List<Post> postList = postRepository.findAllById(postIdList);
        List<PostDTO.Summary> summaries = postList.stream()
                .filter(post -> post.getStatus().equals(Post.PostStatus.PUBLIC))
                .map(DTOMapper::postToSummary)
                .toList();

        return new PageImpl<>(summaries, pageable, searchHits.getTotalHits());
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        User user = post.getUser();
        user.unposting(postId);
        post.softDelete();
        userRepository.save(user);
        postRepository.save(post);
        redisTemplate.delete(cacheName + ":" + user.getUserId());
    }

    @Transactional
    public void updatePost(Long postId, PostDTO.Update update) throws IOException {
        Post post = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        User user = post.getUser();
        post.updatePost(update.getTitle(), update.getContent());
        if (update.getTags() != null) {
            post.removeTagAll();
            post.addTagAll(update.getTags());
        }
        post.updateStatus(update.getStatus());
        String commitMessage = update.getCommitMessage() == null || update.getCommitMessage().isEmpty() ? "Commit" : update.getCommitMessage();
        if (update.getFile() != null) {
            int nextVersion = post.getNextModelVersion();
            String newFileName = fileService.saveFileStorage(update.getFile(), FileService.Extension.GLB);
            Model newModel = Model.builder()
                    .post(post)
                    .version(nextVersion)
                    .fileName(newFileName)
                    .commitMessage(commitMessage)
                    .build();
            post.addModel(newModel);
        }
        Contribution contribution = new Contribution();
        contribution.setUser(user);
        user.updateContribution(contribution);
        post.updateModifiedAt();
        postRepository.save(post);
        userRepository.save(user);
        redisTemplate.delete(cacheName + ":" + user.getUserId());
    }

}
