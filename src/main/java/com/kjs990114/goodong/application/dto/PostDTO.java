package com.kjs990114.goodong.application.dto;


import com.kjs990114.goodong.domain.like.Like;
import com.kjs990114.goodong.domain.post.Model;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.post.Post.PostStatus;
import com.kjs990114.goodong.domain.post.Tag;
import com.kjs990114.goodong.domain.user.User;
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
        private String title;
        private String content;
        private MultipartFile fileGlb;
        private MultipartFile filePng;
        private PostStatus status;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class PostUpdateDTO {
        private String title;
        private String content;
        private MultipartFile fileGlb;
        private String commitMessage;
        private PostStatus status;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
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
        private String profileImage;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private List<String> tags;
        private List<CommentInfoDTO> comments;
        private Long likes;
        @Builder.Default
        private Boolean liked = false;

        public static PostDetailDTO of(PostInfoDTO post, List<ModelInfoDTO> modelInfoDTOs, List<CommentInfoDTO> commentInfoDTOs) {

            return PostDetailDTO.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .status(post.getStatus())
                    .userId(post.getUserId())
                    .email(post.getEmail())
                    .nickname(post.getNickname())
                    .profileImage(post.getProfileImage())
                    .createdAt(post.getCreatedAt())
                    .lastModifiedAt(post.getLastModifiedAt())
                    .likes(post.getLikes())
                    .liked(post.getLiked())
                    .tags(post.getTags())
                    .models(modelInfoDTOs)
                    .comments(commentInfoDTOs)
                    .build();
        }
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDTO {
        private String content;
    }


    @Data
    @Builder
    @AllArgsConstructor
    public static class PostMetadataDTO {
        private String title;
        private String content;
        private List<String> tags;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class PostInfo {
        private PostStatus status;
        private Long userId;
    }


}
