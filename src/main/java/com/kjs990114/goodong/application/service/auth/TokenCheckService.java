package com.kjs990114.goodong.application.service.auth;

import com.kjs990114.goodong.adapter.in.web.dto.UserDTO.UserSummary;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase;
import com.kjs990114.goodong.application.port.out.LoadUserPort;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenCheckService implements CheckTokenUseCase {
    private final JwtUtil jwtUtil;
    private final LoadUserPort loadUserPort;

    @Transactional(readOnly = true)
    @Override
    public UserSummary checkToken(TokenQuery token) {
        String email = jwtUtil.getEmail(token.getJwt());
        User user = loadUserPort.loadByUserEmail(email);
        return UserSummary.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .userId(user.getUserId())
                .build();
    }
}
