package com.kjs990114.goodong.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserDTO {

    @Getter
    public static class RegisterRequest {
        private String email;
        private String password;
        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private String email;
        private String nickname;
    }

    @Getter
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Getter
    public static class UpdateRequest {
        private String nickname;
        private String profileImage;
    }

    @Getter
    @AllArgsConstructor
    public static class LoginResponse {
        private String jwt;
    }
}
