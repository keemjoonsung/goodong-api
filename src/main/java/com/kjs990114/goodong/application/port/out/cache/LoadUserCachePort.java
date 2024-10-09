package com.kjs990114.goodong.application.port.out.cache;

import com.kjs990114.goodong.application.dto.UserSummaryDTO;

public interface LoadUserCachePort {
    UserSummaryDTO loadUserDTO(Long userId, String token);
}
