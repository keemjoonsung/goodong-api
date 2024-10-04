package com.kjs990114.goodong.application.port.out.db;

import com.kjs990114.goodong.application.dto.UserDetailDTO;
import com.kjs990114.goodong.domain.user.User;

public interface LoadUserPort {
    User loadByUserId(Long userId);
    User loadByUserEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    UserDetailDTO loadUserInfoByUserIdBasedOnViewerId(Long userId, Long viewerId);
}
