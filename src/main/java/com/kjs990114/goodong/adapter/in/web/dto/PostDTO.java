package com.kjs990114.goodong.adapter.in.web.dto;


import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.post.Post.PostStatus;
import com.kjs990114.goodong.domain.post.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class PostDTO {


    @Data
    @Builder
    @AllArgsConstructor
    public static class PostCreateDTO {
        @NotBlank(message = "title cannot be blank")
        private String title;
        @NotBlank(message = "Content cannot be blank")
        private String content;
        private MultipartFile file;
        private PostStatus status;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class PostUpdateDTO {
        @NotBlank(message = "title cannot be blank")
        private String title;
        @NotBlank(message = "content cannot be blank")
        private String content;
        private MultipartFile file;
        @NotBlank(message = "commit message cannot be blank")
        private String commitMessage;
        private PostStatus status;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostSummaryDTO {
        private Long postId;
        private String title;
        private Long userId;
        private String email;
        private String nickname;
        private PostStatus status;
        private LocalDateTime lastModifiedAt;
        private List<String> tags;
        private int likes;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class PostDetailDTO {
        private Long postId;
        private String title;
        private String content;
        private PostStatus status;
        private List<ModelInfoDTO> models;
        private Long userId;
        private String email;
        private String nickname;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private List<String> tags;
        private List<CommentInfo> comments;
        private int likes;
        @Builder.Default
        private Boolean liked = false;

        public static PostDetailDTO of(Post post, boolean liked){
            return PostDetailDTO.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .status(post.getStatus())
                    .models(post.getModels().stream()
                            .map(model -> ModelInfoDTO.builder()
                                    .version(model.getVersion())
                                    .fileName(model.getFileName())
                                    .commitMessage(model.getCommitMessage())
                                    .build())
                            .toList())
                    .userId(post.getUser().getUserId())
                    .email(post.getUser().getEmail())
                    .nickname(post.getUser().getNickname())
                    .createdAt(post.getCreatedAt())
                    .lastModifiedAt(post.getLastModifiedAt())
                    .tags(post.getTags().stream()
                            .map(Tag::getTag)
                            .toList())
                    .comments(post.getComments().stream()
                            .map(comment -> CommentInfo.builder()
                                    .commentId(comment.getCommentId())
                                    .userId(comment.getUser().getUserId())
                                    .email(comment.getUser().getEmail())
                                    .nickname(comment.getUser().getNickname())
                                    .content(comment.getContent())
                                    .createdAt(comment.getCreatedAt())
                                    .lastModifiedAt(comment.getLastModifiedAt())
                                    .build())
                            .toList())
                    .likes(post.getLikes().size())  // likes 수 계산
                    .liked(liked)  // 현재 사용자가 좋아요를 눌렀는지 여부
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class CommentInfo {
        private Long commentId;
        private Long userId;
        private String email;
        private String nickname;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class CommentDTO {
        private String content;
    }

    @Getter
    @Builder
    public static class ModelInfoDTO {
        private Integer version;
        private String fileName;
        private String commitMessage;
    }


    @Data
    @Builder
    @AllArgsConstructor
    public static class AiResponse {
        private String title;
        private String description;
        private List<String> tags;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class PostInfo {
        private PostStatus status;
        private Long userId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class files {
        private MultipartFile file;
        private MultipartFile fileGlb;
    }

}
