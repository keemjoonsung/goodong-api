package com.kjs990114.goodong.application.dto;

import com.kjs990114.goodong.domain.post.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class PostInfoDTO {
    private Long postId;
    private String title;
    private String content;
    private Post.PostStatus status;
    private Long userId;
    private String email;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private List<String> tags;
    private Long likes;
    private Boolean liked;

    public PostInfoDTO(Long postId, String title, String content, Post.PostStatus status,
                       Long userId, String email, String nickname,
                       LocalDateTime createdAt, LocalDateTime lastModifiedAt,
                       Object tags, Long likes, Boolean liked) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.status = status;
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.tags = tags != null ? Arrays.stream(((String)tags).split(",")).sorted().toList() : new ArrayList<>();
        this.likes = likes;
        this.liked = liked;

    }
}
