package com.kjs990114.goodong.presentation.dto;


import com.kjs990114.goodong.domain.post.Post;
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
    public static class Create{
        @NotBlank(message = "title cannot be blank")
        private String title;
        @NotBlank(message = "Content cannot be blank")
        private String content;
        private MultipartFile file;
        private Post.PostStatus status;
        @NotBlank(message = "Email cannot be blank")
        private String commitMessage;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Update{
        private String title;
        private String content;
        private MultipartFile file;
        private String commitMessage;
        private Post.PostStatus status;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Summary {
        private Long postId;
        private String title;
        private Long userId;
        private String email;
        private String nickname;
        private Post.PostStatus status;
        private LocalDateTime lastModifiedAt;
        private List<String> tags;
        private Integer likes;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class PostDetail {
        private Long postId;
        private String title;
        private String content;
        private Post.PostStatus status;
        private List<ModelInfo> models;
        private Long userId;
        private String email;
        private String nickname;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private List<String> tags;
        private List<CommentInfo> comments;
        private Integer likes;
        @Builder.Default
        private Boolean liked = false;
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
    public static class PostComment {
        private String content;
    }

    @Getter
    @Builder
    public static class ModelInfo {
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
        private Post.PostStatus status;
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
