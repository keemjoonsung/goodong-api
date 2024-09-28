package com.kjs990114.goodong.application.port.out;

public interface LoadUserPort {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
