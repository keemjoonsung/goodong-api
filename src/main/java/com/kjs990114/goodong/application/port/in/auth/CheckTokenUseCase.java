package com.kjs990114.goodong.application.port.in.auth;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface CheckTokenUseCase {
    UserSummaryDTO checkToken(TokenQuery token);

    @Getter
    @AllArgsConstructor
    class TokenQuery{
        String jwt;


    }
}
