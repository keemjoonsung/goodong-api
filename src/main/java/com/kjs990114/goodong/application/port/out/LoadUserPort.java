package com.kjs990114.goodong.application.port.out;

import com.kjs990114.goodong.domain.user.User;

public interface LoadUserPort {
    User loadByUserId(Long userId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
