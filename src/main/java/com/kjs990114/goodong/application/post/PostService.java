package com.kjs990114.goodong.application.post;

import com.kjs990114.goodong.application.file.FileService;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.domain.post.*;
import com.kjs990114.goodong.domain.post.repository.PostRepository;
import com.kjs990114.goodong.domain.post.repository.PostSearchRepository;
import com.kjs990114.goodong.domain.user.Contribution;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import com.kjs990114.goodong.infrastructure.PostDocument;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "postsPrivate", key = "#userId"),
            @CacheEvict(value = "postsPublic", key = "#userId"),
            @CacheEvict(value = "contributions", key = "#userId")
    })
    public void createPost(PostDTO.Create create, Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
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
        String commitMsg = create.getCommitMessage() == null || create.getCommitMessage().isBlank()? "Commit" : create.getCommitMessage();
        Model newModel = Model.builder()
                .commitMessage(commitMsg)
                .post(newPost)
                .fileName(fileName)
                .version(1)
                .build();

        newPost.addModel(newModel);
        newPost.addTagAll(create.getTags());
        user.posting(newPost);
        userRepository.save(user);

        Post post = postRepository.save(newPost);
        List<Tag> tags = post.getTags();
        String tagging = tags.stream()
                .map(Tag::getTag)
                .collect(Collectors.joining(" "));

        PostDocument postDocument = new PostDocument();
        postDocument.setPostId(post.getPostId());
        postDocument.setTitle(post.getTitle());
        postDocument.setContent(post.getContent());
        postDocument.setTagging(tagging);
        postSearchRepository.save(postDocument);
    }

    @Transactional(readOnly = true)
    public Boolean checkDuplicatedTitle(String title, String token) {
        String email = jwtUtil.getEmail(token);
        User user = userRepository.findByEmail(email).orElseThrow();
        return user.getPosts().stream()
                .anyMatch(post -> post.getTitle().equalsIgnoreCase(title));
    }

    @Transactional(readOnly = true)
    public PostDTO.PostInfo getPost(String fileName){
        Post post = postRepository.findPostIdByFileName(fileName).orElseThrow(()->new NotFoundException("Model does not exist"));
        return new PostDTO.PostInfo(post.getStatus(),post.getUser().getUserId());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "postsPublic", key = "#userId")
    public List<PostDTO.Summary> getUserPublicPosts(Long userId) {
        return getUserPosts(userId, Post.PostStatus.PUBLIC);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "postsPrivate", key = "#userId")
    public List<PostDTO.Summary> getUserPrivatePosts(Long userId) {
        return getUserPosts(userId, Post.PostStatus.PRIVATE);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "postDetail", key = "#postId")
    public PostDTO.PostDetail getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("User does not exists"));
        User user = post.getUser();

        List<Model> modelsEntity = post.getModels();
        List<PostDTO.ModelInfo> models = modelsEntity.stream().map(model ->
                PostDTO.ModelInfo.builder()
                        .version(model.getVersion())
                        .fileName(model.getFileName())
                        .commitMessage(model.getCommitMessage())
                        .build()
        ).toList();

        return PostDTO.PostDetail.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .models(models)
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .tags(post.getTags().stream().map(Tag::getTag).collect(Collectors.toList()))
                .likes(post.getLikes().size())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PostDTO.Summary> searchPosts(String keyword,Long userId) {
        List<PostDocument> documents = postSearchRepository.findByTitleContainingOrContentContainingOrTaggingContaining(keyword, keyword, keyword);
        if(documents.isEmpty()) return new ArrayList<>();
        System.out.println(documents.size());
        return documents.stream().filter(document ->{
            Post post = postRepository.findById(document.getPostId()).orElseThrow();
            User user = post.getUser();
            return user.getUserId().equals(userId) || post.getStatus() == Post.PostStatus.PUBLIC;
        }).map(document -> {
                    Post post = postRepository.findById(document.getPostId()).orElseThrow();
                    List<Tag> tags = post.getTags();
                    User user = post.getUser();
                    return PostDTO.Summary.builder()
                            .postId(post.getPostId())
                            .title(post.getTitle())
                            .userId(user.getUserId())
                            .email(user.getEmail())
                            .nickname(user.getNickname())
                            .status(post.getStatus())
                            .lastModifiedAt(post.getLastModifiedAt())
                            .tags(tags.stream().map(Tag::getTag).collect(Collectors.toList()))
                            .likes(post.getLikes().size())
                            .build();
                }
        ).toList();
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "postDetail", key = "#postId"),
            @CacheEvict(value = "likeList", allEntries = true)
    })
    @CacheEvict(value = "postDetail", key = "#postId")
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        PostDocument postDocument = postSearchRepository.findById(post.getPostId()).orElseThrow(() -> new NotFoundException("Post does not exist"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exist"));
        user.unposting(postId);
        userRepository.save(user);
        postRepository.delete(post);
        postSearchRepository.delete(postDocument);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "postDetail", key = "#postId"),
            @CacheEvict(value = "contributions", key = "#userId"),
            @CacheEvict(value = "postsPublic", key = "#userId"),
            @CacheEvict(value = "postsPrivate", key = "#userId")
    })
    public void updatePost(Long postId, Long userId, PostDTO.Update update) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exist"));
        PostDocument postDocument = postSearchRepository.findByPostId(post.getPostId()).orElseThrow(() -> new NotFoundException("Post does not exist"));
        post.updatePost(update.getTitle(), update.getContent());
        if(update.getTags() != null) {
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
        postDocument.setContent(update.getContent());
        postDocument.setTitle(update.getTitle());
        if(update.getTags() != null) {
            postDocument.setTagging(String.join(" ", update.getTags()));
        }
        Contribution contribution = new Contribution();
        contribution.setUser(user);
        user.updateContribution(contribution);
        post.updateModifiedAt();
        postRepository.save(post);
        postSearchRepository.save(postDocument);
        userRepository.save(user);

    }

    private List<PostDTO.Summary> getUserPosts(Long userId, Post.PostStatus type) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        List<Post> posts = user.getPosts();
        return posts.stream()
                .filter(post -> post.getStatus() == type)
                .map(post -> {
                    List<Tag> tags = post.getTags();
                    return PostDTO.Summary.builder()
                            .postId(post.getPostId())
                            .title(post.getTitle())
                            .userId(user.getUserId())
                            .email(user.getEmail())
                            .nickname(user.getNickname())
                            .status(post.getStatus())
                            .lastModifiedAt(post.getLastModifiedAt())
                            .tags(tags.stream().map(Tag::getTag).collect(Collectors.toList()))
                            .build();
                }).collect(Collectors.toList());
    }


}
