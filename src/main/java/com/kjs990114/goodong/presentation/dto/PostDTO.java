package com.kjs990114.goodong.presentation.dto;


import com.kjs990114.goodong.domain.post.Post;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@Builder
public class PostDTO {


    @Getter
    @Builder
    public static class Create{
        private String title;
        private String content;
        private MultipartFile file;
        private Post.PostStatus status;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
    }

    @Getter
    @Builder
    public static class Update{
        private String title;
        private String content;
        private MultipartFile file;
        private String commitMessage;
        private Post.PostStatus status;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
    }

    @Getter
    @Builder
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

    @Getter
    @Builder
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
    }
    @Getter
    @Builder
    public static class CommentInfo {
        private Long commentId;
        private Long userId;
        private String email;
        private String nickname;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
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

    @Getter
    @Setter
    @AllArgsConstructor
    public static class File {
        private MultipartFile file;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AiResponse {
        private String title;
        private String description;
        private List<String> tags;

    }

}
