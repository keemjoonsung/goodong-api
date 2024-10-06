package com.kjs990114.goodong.application.port.in.social;

import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetFollowersUseCase {
    Page<UserSummaryDTO> getFollowers(GetFollowersQuery getFollowersQuery);

    @Getter
    @AllArgsConstructor
    class GetFollowersQuery{
        private Long userId;
        private Pageable pageable;
    }
}
