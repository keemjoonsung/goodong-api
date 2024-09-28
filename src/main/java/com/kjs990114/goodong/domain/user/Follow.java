package com.kjs990114.goodong.domain.user;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

    private Long id;
    private User follower;
    private User followee;
}