package com.kjs990114.goodong.application.port.out;

import com.kjs990114.goodong.domain.user.User;

public interface DeleteUserPort {

    void delete(Long userId);
}