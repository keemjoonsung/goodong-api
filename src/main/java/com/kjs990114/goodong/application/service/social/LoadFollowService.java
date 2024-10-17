package com.kjs990114.goodong.application.service.social;

import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import com.kjs990114.goodong.application.port.in.social.GetFollowersUseCase;
import com.kjs990114.goodong.application.port.in.social.GetFollowingsUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadFollowPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoadFollowService implements GetFollowersUseCase, GetFollowingsUseCase {

    private final LoadFollowPort loadFollowPort;
    @Value("${spring.cloud.gcp.storage.path}${spring.cloud.gcp.storage.bucket}/")
    private String baseUrl;

    @Transactional(readOnly = true)
    @Override
    public Page<UserSummaryDTO> getFollowers(GetFollowersQuery getFollowersQuery) {
        Page<UserSummaryDTO> response = loadFollowPort.loadFollowers(getFollowersQuery.getUserId(), getFollowersQuery.getPageable());
        response.map(userSummaryDTO -> {
            userSummaryDTO.setProfileImage(baseUrl + userSummaryDTO.getProfileImage());
            return userSummaryDTO;
        });
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserSummaryDTO> getFollowings(GetFollowingsQuery getFollowingsQuery) {
        Page<UserSummaryDTO> response =  loadFollowPort.loadFollowings(getFollowingsQuery.getUserId(),getFollowingsQuery.getPageable());
        response.map(userSummaryDTO -> {
            userSummaryDTO.setProfileImage(baseUrl + userSummaryDTO.getProfileImage());
            return userSummaryDTO;
        });
        return response;
    }
}
