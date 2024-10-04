package com.kjs990114.goodong.domain.comment;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Long commentId;
    private String content;
    private Long userId;
    private Long postId;

    public static Comment of(Long userId, Long postId){
        return Comment.builder().userId(userId).postId(postId).build();
    }

    public void updateContent(String content){
        if(!content.isBlank()){
            this.content = content;
        }
    }
}