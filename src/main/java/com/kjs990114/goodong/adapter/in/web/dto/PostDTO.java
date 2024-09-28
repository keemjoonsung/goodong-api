package com.kjs990114.goodong.adapter.in.web.dto;


import com.kjs990114.goodong.adapter.out.persistence.entity.PostEntity;
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
        private PostEntity.PostStatus status;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Update{
        @NotBlank(message = "title cannot be blank")
        private String title;
        @NotBlank(message = "content cannot be blank")
        private String content;
        private MultipartFile file;
        @NotBlank(message = "commit message cannot be blank")
        private String commitMessage;
        private PostEntity.PostStatus status;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Summary {
        private Long postId;
        private String title;
        private Long userId;
        private String email;
        private String nickname;
        private PostEntity.PostStatus status;
        private LocalDateTime lastModifiedAt;
        private List<String> tags;
        private int likes;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class PostDetail {
        private Long postId;
        private String title;
        private String content;
        private PostEntity.PostStatus status;
        private List<ModelInfo> models;
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
        private PostEntity.PostStatus status;
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
