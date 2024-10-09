package com.kjs990114.goodong.application.service.auth;

import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase;
import com.kjs990114.goodong.application.port.out.cache.LoadUserCachePort;
import com.kjs990114.goodong.application.port.out.cache.SaveUserCachePort;
import com.kjs990114.goodong.application.port.out.db.LoadUserPort;
import com.kjs990114.goodong.common.jwt.JwtUtil;
import com.kjs990114.goodong.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckTokenService implements CheckTokenUseCase {
    private final JwtUtil jwtUtil;
    private final LoadUserPort loadUserPort;
    private final LoadUserCachePort loadUserCachePort;
    private final SaveUserCachePort saveUserCachePort;
    @Transactional(readOnly = true)
    @Override
    public UserSummaryDTO checkToken(TokenQuery token) {
        Long userId = jwtUtil.getUserId(token.getJwt());
        UserSummaryDTO cached = loadUserCachePort.loadUserDTO(userId, token.getJwt());
        if(cached != null) return cached;
        User user = loadUserPort.loadByUserId(userId);
        UserSummaryDTO stored =  UserSummaryDTO.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .userId(user.getUserId())
                .build();
        saveUserCachePort.saveUserDTO(userId,token.getJwt(),stored);
        return stored;
    }

    @Override
    public Long getUserId(TokenQuery token) {
        return jwtUtil.getUserId(token.getJwt());
    }
}
