package com.kjs990114.goodong.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class UserDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Register {
        private String email;
        private String password;
        private String nickname;
    }
    //간단한 유저 정보
    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserSummary {
        private Long userId;
        private String email;
        private String nickname;
        private String profileImage;
    }
    //유저 상세 정보
    @Getter
    @Builder
    public static class UserDetail {
        private Long userId;
        private String email;
        private String nickname;
        private String profileImage;
        private int followerCount;
        private int followingCount;
        private List<UserContribution> userContributions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Login {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserContribution {
        LocalDate date;
        int count;
    }

    @Getter
    @Setter
    public static class UpdateNickname {
        private String nickname;
    }
    
    @Getter
    @Setter
    public static class UpdateProfileImage {
        private String profileImage;
    }

    @Getter
    @Setter
    public static class Password{
        private String password;
    }

}
