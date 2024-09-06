package com.kjs990114.goodong.application.post;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.domain.post.*;
import com.kjs990114.goodong.domain.post.repository.PostRepository;
import com.kjs990114.goodong.domain.post.repository.PostSearchRepository;
import com.kjs990114.goodong.domain.user.Contribution;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import com.kjs990114.goodong.infrastructure.PostDocument;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    private final Storage storage;
    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;
    private final UserRepository userRepository;

    /**
     * 포스트를 완전 처음 생성하면, user의 contribution이 1 늘어난다
     **/
    public void createPost(PostDTO.Create create, String email) throws IOException {
        User user = userRepository.findByEmail(email).orElseThrow();
        Contribution contribution = new Contribution();
        contribution.setUser(user);
        user.updateContribution(contribution);

        Post newPost = Post.builder()
                .title(create.getTitle())
                .content(create.getContent())
                .status(create.getStatus())
                .build();
        String url = generateFileUrl(create.getFile());
        Model newModel = com.kjs990114.goodong.domain.post.Model.builder()
                .commitMessage("First Commit")
                .post(newPost)
                .fileUrl(url)
                .version(1)
                .build();

        newPost.addModel(newModel);
        newPost.addTagAll(create.getTags());
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

    public List<PostDTO.Summary> getUserPosts(String email, boolean isMyPosts) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Post> posts = postRepository.findAllByUser(user);
        return posts.stream()
                .filter(post -> isMyPosts || post.getStatus() == Post.PostStatus.PUBLIC)
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
                            .likes(post.getLikes().size())
                            .build();
                }).collect(Collectors.toList());
    }

    public PostDTO.PostDetail getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = post.getUser();

        List<Model> modelsEntity = post.getModels();
        List<PostDTO.ModelInfo> models = modelsEntity.stream().map(model ->
                PostDTO.ModelInfo.builder()
                        .version(model.getVersion())
                        .fileUrl(model.getFileUrl())
                        .commitMessage(model.getCommitMessage())
                        .build()
        ).toList();

        List<Comment> commentsEntity = post.getComments();
        List<PostDTO.CommentInfo> comments = commentsEntity.stream()
                .map(comment -> {
                    User commentUser = comment.getUser();
                    return PostDTO.CommentInfo.builder()
                            .commentId(comment.getCommentId())
                            .userId(commentUser.getUserId())
                            .email(commentUser.getEmail())
                            .nickname(commentUser.getNickname())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .lastModifiedAt(comment.getLastModifiedAt())
                            .build();
                }).toList();

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
                .comments(comments)
                .likes(post.getLikes().size())
                .build();
    }

    public List<PostDTO.Summary> searchPosts(String keyword) {
        List<PostDocument> documents = postSearchRepository.findByTitleContainingOrContentContainingOrTaggingContaining(keyword, keyword, keyword);
        return documents.stream().map(document -> {
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

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GlobalException("Post does not exist"));
        PostDocument postDocument = postSearchRepository.findById(post.getPostId()).orElseThrow(() -> new GlobalException("Post does not exist"));

        postRepository.delete(post);
        postSearchRepository.delete(postDocument);

    }

    public void updatePost(Long postId, PostDTO.Update update) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GlobalException("Post does not exist"));
        User user = post.getUser();
        PostDocument postDocument = postSearchRepository.findByPostId(post.getPostId()).orElseThrow(() -> new GlobalException("Post does not exist"));
        post.updatePost(update.getTitle(), update.getContent());
        post.removeTagAll();
        post.addTagAll(update.getTags());
        post.updateStatus(update.getStatus());
        if (update.getFile() != null) {
            int nextVersion = post.getNextModelVersion();
            String newFileUrl = generateFileUrl(update.getFile());
            Model newModel = Model.builder()
                    .post(post)
                    .version(nextVersion)
                    .fileUrl(newFileUrl)
                    .commitMessage(update.getCommitMessage())
                    .build();
            post.addModel(newModel);
        }

        postDocument.setContent(update.getContent());
        postDocument.setTitle(update.getTitle());
        postDocument.setTagging(String.join(" ", update.getTags()));

        Contribution contribution = new Contribution();
        contribution.setUser(user);
        user.updateContribution(contribution);

        postRepository.save(post);
        postSearchRepository.save(postDocument);
        userRepository.save(user);

    }


    public Resource getFileResource(String fileUrl) {
        Blob blob = storage.get(bucketName, fileUrl);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blob.downloadTo(outputStream);

        return new ByteArrayResource(outputStream.toByteArray());
    }

    private String generateFileUrl(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + ".glb";

        storage.create(
                BlobInfo.newBuilder(bucketName, fileName).build(),
                file.getBytes()
        );

        return String.format("%s/%s", bucketName, fileName);

    }

}
