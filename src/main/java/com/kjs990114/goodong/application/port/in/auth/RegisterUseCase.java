package com.kjs990114.goodong.application.port.in.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface RegisterUseCase {
    void register(RegisterCommand registerCommand);
    boolean isDuplicateNickname(String nickname);
    boolean isDuplicateEmail(String email);
    boolean isValidPassword(String password);

    @Getter
    @AllArgsConstructor
    class RegisterCommand {
        private String email;
        private String nickname;
        private String password;
    }
}
