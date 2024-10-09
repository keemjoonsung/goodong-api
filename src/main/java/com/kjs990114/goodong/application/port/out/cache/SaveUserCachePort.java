package com.kjs990114.goodong.application.port.out.cache;

import com.kjs990114.goodong.application.dto.UserSummaryDTO;

public interface SaveUserCachePort {
    void saveUserDTO(Long userId, String token, UserSummaryDTO userSummaryDTO);
}
