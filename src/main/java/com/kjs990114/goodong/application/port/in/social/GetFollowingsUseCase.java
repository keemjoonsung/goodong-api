package com.kjs990114.goodong.application.port.in.social;

import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetFollowingsUseCase {
    Page<UserSummaryDTO> getFollowings(GetFollowingsQuery getFollowingsQuery);

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class GetFollowingsQuery{
        private Long userId;
        private Pageable pageable;
    }
}
