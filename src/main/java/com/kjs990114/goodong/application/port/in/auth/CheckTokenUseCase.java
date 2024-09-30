package com.kjs990114.goodong.application.port.in.auth;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.kjs990114.goodong.adapter.in.web.dto.UserDTO.*;

public interface CheckTokenUseCase {
    UserSummaryDTO checkToken(TokenQuery token);

    @Getter
    @AllArgsConstructor
    class TokenQuery{
        String jwt;


    }
}
