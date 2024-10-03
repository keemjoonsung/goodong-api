package com.kjs990114.goodong.domain.follow;


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