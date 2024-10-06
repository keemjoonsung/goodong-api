package com.kjs990114.goodong.application.dto;

import com.kjs990114.goodong.domain.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class PostSummaryDTO {
    private Long postId;
    private String title;
    private Long userId;
    private String email;
    private String nickname;
    private Post.PostStatus status;
    private LocalDateTime lastModifiedAt;
    private List<String> tags;
    private Long likes;

    public PostSummaryDTO(Long postId, String title, Long userId, String email, String nickname, Post.PostStatus status, LocalDateTime lastModifiedAt, Object tags, Long likes) {
        this.postId = postId;
        this.title = title;
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.status = status;
        this.lastModifiedAt = lastModifiedAt;
        this.tags = tags != null ? Arrays.stream(((String) tags).split(",")).sorted().toList() : new ArrayList<>();
        this.likes = likes;
    }
}
