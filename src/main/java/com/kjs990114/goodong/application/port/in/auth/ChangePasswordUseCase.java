package com.kjs990114.goodong.application.port.in.auth;


import lombok.AllArgsConstructor;
import lombok.Getter;

public interface ChangePasswordUseCase {
    void changePassword(PasswordQuery passwordQuery);

    @Getter
    @AllArgsConstructor
    class PasswordQuery {
        Long userId;
        String password;

    }
}
