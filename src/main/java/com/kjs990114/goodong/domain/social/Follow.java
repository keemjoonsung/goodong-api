package com.kjs990114.goodong.domain.social;


import com.kjs990114.goodong.domain.user.User;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

    private Long followId;
    private Long followerId;
    private Long followeeId;
}