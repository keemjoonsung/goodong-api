package com.kjs990114.goodong.presentation.dto;


import com.kjs990114.goodong.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    }
    @Getter
    @Builder
    public static class Summary {
        private Long postId;
        private String title;
        private String ownerEmail;
        private String ownerNickname;
        private Post.PostStatus status;
        private LocalDateTime lastModifiedAt;
        private List<String> tags;
        private Integer likes;
    }

    @Getter
    @Builder
    public static class Detail {
        private Long postId;
        private String title;
        private String content;
        private Post.PostStatus status;
        private List<ModelInfo> models;
        private String ownerEmail;
        private String ownerNickname;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private List<String> tags;
        private List<CommentInfo> comments;
        private Integer likes;
    }
    @Getter
    @Builder
    public static class CommentInfo {
        private Long userId;
        private String email;
        private String nickname;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
    }
    @Getter
    @Builder
    public static class ModelInfo {
        private Integer version;
        private String fileUrl;
        private String commitMessage;
    }

}
