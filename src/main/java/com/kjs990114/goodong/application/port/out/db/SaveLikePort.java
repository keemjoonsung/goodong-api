package com.kjs990114.goodong.application.port.out.db;

import com.kjs990114.goodong.domain.like.Like;

public interface SaveLikePort {
    Long save(Like like);
}
