package com.kjs990114.goodong.application.service.social;

import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import com.kjs990114.goodong.application.port.in.social.GetFollowersUseCase;
import com.kjs990114.goodong.application.port.in.social.GetFollowingsUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadFollowPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoadFollowService implements GetFollowersUseCase, GetFollowingsUseCase {

    private final LoadFollowPort loadFollowPort;

    @Transactional(readOnly = true)
    @Override
    public Page<UserSummaryDTO> getFollowers(GetFollowersQuery getFollowersQuery) {
        return loadFollowPort.loadFollowers(getFollowersQuery.getUserId(), getFollowersQuery.getPageable());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserSummaryDTO> getFollowings(GetFollowingsQuery getFollowingsQuery) {
        return loadFollowPort.loadFollowings(getFollowingsQuery.getUserId(),getFollowingsQuery.getPageable());
    }
}
