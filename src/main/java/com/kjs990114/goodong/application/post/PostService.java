package com.kjs990114.goodong.application.post;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.domain.post.Comment;
import com.kjs990114.goodong.domain.post.Model;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.post.Tag;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final JwtUtil jwtUtil;

    /**
     * 포스트를 완전 처음 생성하면, user의 contribution이 1 늘어난다
     **/
    public void createPost(PostDTO.Create create, String token) throws IOException {
        User user = userRepository.findByEmail(jwtUtil.getEmail(token)).orElseThrow();
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

        for (String tag : create.getTags()) {
            Tag newTag = Tag.builder()
                    .tag(tag)
                    .build();
            newPost.addTag(newTag);
        }

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

    public List<PostDTO.Summary> getUserPosts(String email, String token) {
        User user = userRepository.findByEmail(email).orElseThrow();
        boolean isMyPosts = jwtUtil.getEmail(token).equals(email);
        List<Post> posts = postRepository.findAllByUser(user);
        return posts.stream()
                .filter(post -> isMyPosts || post.getStatus() == Post.PostStatus.PUBLIC)
                .map(post -> {
                    List<Tag> tags = post.getTags();
                    return PostDTO.Summary.builder()
                            .postId(post.getPostId())
                            .title(post.getTitle())
                            .ownerEmail(user.getEmail())
                            .ownerNickname(user.getNickname())
                            .status(post.getStatus())
                            .lastModifiedAt(post.getLastModifiedAt())
                            .tags(tags.stream().map(Tag::getTag).collect(Collectors.toList()))
                            .likes(post.getLikes().size())
                            .build();
                }).collect(Collectors.toList());
    }

    public PostDTO.Detail getPost(Long postId) {
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
                .map(comment ->{
                    User commentUser = comment.getUser();
                    return PostDTO.CommentInfo.builder()
                            .userId(commentUser.getUserId())
                            .email(commentUser.getEmail())
                            .nickname(commentUser.getNickname())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .lastModifiedAt(comment.getLastModifiedAt())
                            .build();
                }).toList();

        return PostDTO.Detail.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .models(models)
                .ownerEmail(user.getEmail())
                .ownerNickname(user.getNickname())
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
                            .ownerEmail(user.getEmail())
                            .ownerNickname(user.getNickname())
                            .status(post.getStatus())
                            .lastModifiedAt(post.getLastModifiedAt())
                            .tags(tags.stream().map(Tag::getTag).collect(Collectors.toList()))
                            .likes(post.getLikes().size())
                            .build();
                }
        ).toList();
    }

    private String generateFileUrl(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + ".glb";

        storage.create(
                BlobInfo.newBuilder(bucketName, fileName).build(),
                file.getBytes()
        );

        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);

    }

}
