package com.kjs990114.goodong.application.port.out.mysql;

import com.kjs990114.goodong.domain.user.User;

public interface SaveUserPort {
     User save(User user);
}
