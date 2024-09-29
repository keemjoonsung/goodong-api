package com.kjs990114.goodong.domain.post;

import com.kjs990114.goodong.domain.user.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    private Long likeId;
    private Post post;
    private User user;
}