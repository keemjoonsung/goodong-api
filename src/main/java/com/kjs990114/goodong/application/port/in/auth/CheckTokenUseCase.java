package com.kjs990114.goodong.application.port.in.auth;

import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface CheckTokenUseCase {
    UserSummaryDTO checkToken(TokenQuery token);
    Long getUserId(TokenQuery token);

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class TokenQuery{
        private String jwt;
    }
}
