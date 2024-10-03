package com.kjs990114.goodong.application.port.out.db;

import com.kjs990114.goodong.domain.user.User;

public interface SaveUserPort {
     Long save(User user);
}
