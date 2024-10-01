package com.kjs990114.goodong.application.port.in.user;

import com.kjs990114.goodong.application.dto.UserDTO.ContributionsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface GetUserContributionUseCase {

    ContributionsDTO getContributions(LoadContributionQuery loadContributionQuery);

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class LoadContributionQuery{
        private Long userId;
    }
}
