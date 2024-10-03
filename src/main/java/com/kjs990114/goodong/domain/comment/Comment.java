package com.kjs990114.goodong.domain.comment;


import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Long commentId;
    private String content;
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}