package com.kjs990114.goodong.application.port.out.db;

import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoadFollowPort {

    Page<UserSummaryDTO> loadFollowings(Long userId, Pageable pageable);
    Page<UserSummaryDTO> loadFollowers(Long userId, Pageable pageable);
}
