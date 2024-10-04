package com.kjs990114.goodong.application.port.in.user;

import com.kjs990114.goodong.application.dto.UserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface GetUserInfoUseCase {

    UserInfoDTO getUserInfo(LoadUserInfoQuery loadUserInfoQuery);

    @Getter
    @AllArgsConstructor
    class LoadUserInfoQuery{
        private Long userId;
        private Long viewerId;
    }
}
