package com.kjs990114.goodong.application.post;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.domain.post.Model;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.post.Tag;
import com.kjs990114.goodong.domain.post.repository.PostRepository;
import com.kjs990114.goodong.domain.post.repository.PostSearchRepository;
import com.kjs990114.goodong.domain.user.Contribution;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import com.kjs990114.goodong.infrastructure.post.elasticsearch.PostDocument;
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
    public void createPost(PostDTO.CreateDTO createDTO, String token) throws IOException {
        User user = userRepository.findByEmail(jwtUtil.getEmail(token)).orElseThrow();
        Contribution contribution = new Contribution();
        contribution.setUser(user);
        user.updateContribution(contribution);

        Post newPost = Post.builder()
                .title(createDTO.getTitle())
                .content(createDTO.getContent())
                .build();
        String url = generateFileUrl(createDTO.getFile());
        Model newModel = Model.builder()
                .commitMessage("First Commit")
                .post(newPost)
                .fileUrl(url)
                .build();

        Tag newTag = Tag.builder()
                .tag("태그 1")
                .build();

        newPost.addModel(newModel);
        newPost.addTag(newTag);


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

    public List<PostDTO.SummaryDTO> getUserPosts(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Post> posts = postRepository.findAllByUser(user);
        return posts.stream().map(post -> {
            List<Tag> tags = post.getTags();
            return PostDTO.SummaryDTO.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .ownerEmail(user.getEmail())
                    .ownerNickname(user.getNickname())
                    .lastModifiedAt(post.getLastModifiedAt())
                    .tags(tags.stream().map(Tag::getTag).collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());
    }

    public PostDTO.DetailDTO getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = post.getUser();
        List<Model> modelsEntity = post.getModels();
        List<PostDTO.ModelDTO> models = modelsEntity.stream().map(model ->
                PostDTO.ModelDTO.builder()
                        .version(model.getVersion())
                        .fileUrl(model.getFileUrl())
                        .commitMessage(model.getCommitMessage())
                        .build()
        ).toList();
        return PostDTO.DetailDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .models(models)
                .ownerEmail(user.getEmail())
                .ownerNickname(user.getNickname())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }

    public List<PostDTO.SummaryDTO> searchPosts(String keyword) {
        List<PostDocument> documents = postSearchRepository.findByTitleContainingOrContentContainingOrTaggingContaining(keyword, keyword, keyword);
        return documents.stream().map(document -> {
                    Post post = postRepository.findById(document.getPostId()).orElseThrow();
                    List<Tag> tags = post.getTags();
                    User user = post.getUser();
                    return PostDTO.SummaryDTO.builder()
                            .postId(post.getPostId())
                            .title(post.getTitle())
                            .ownerEmail(user.getEmail())
                            .ownerNickname(user.getNickname())
                            .lastModifiedAt(post.getLastModifiedAt())
                            .tags(tags.stream().map(Tag::getTag).collect(Collectors.toList()))
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