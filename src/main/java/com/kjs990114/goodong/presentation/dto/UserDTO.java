package com.kjs990114.goodong.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserDTO {

    @Getter
    public static class Register {
        private String email;
        private String password;
        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private String email;
        private String nickname;
    }

    @Getter
    public static class Login {
        private String email;
        private String password;
    }

    @Getter
    public static class Update {
        private String nickname;
        private String profileImage;
    }

}
