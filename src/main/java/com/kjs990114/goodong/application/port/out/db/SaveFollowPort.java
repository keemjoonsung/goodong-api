package com.kjs990114.goodong.application.port.out.db;

import com.kjs990114.goodong.domain.follow.Follow;

public interface SaveFollowPort {
    void save(Follow follow);
}
