package com.kjs990114.goodong.domain.post;


import com.kjs990114.goodong.domain.user.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Long commentId;
    private String content;
    private User user;
    private Post post;


}