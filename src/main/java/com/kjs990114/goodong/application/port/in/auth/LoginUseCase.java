package com.kjs990114.goodong.application.port.in.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface LoginUseCase {
    String login(LoginCommand logincommand);
    @Getter
    @AllArgsConstructor
    class LoginCommand{
        private String email;
        private String password;
    }
}
