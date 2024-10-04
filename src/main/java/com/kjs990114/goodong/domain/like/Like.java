package com.kjs990114.goodong.domain.like;

import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.user.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    private Long likeId;
    private Long postId;
    private Long userId;

    public static Like of(Long postId, Long userId){
        return Like.builder()
                .postId(postId)
                .userId(userId)
                .build();
    }
}