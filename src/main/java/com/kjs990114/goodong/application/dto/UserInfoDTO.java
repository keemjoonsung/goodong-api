package com.kjs990114.goodong.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDTO {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImage;
    private Long followerCount;
    private Long followingCount;
    private Boolean followed;
}
