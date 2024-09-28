package com.kjs990114.goodong.domain.post;

import com.kjs990114.goodong.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    private Long likeId;
    private Post post;
    private User user;
}