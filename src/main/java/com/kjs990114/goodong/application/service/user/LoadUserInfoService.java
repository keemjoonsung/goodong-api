package com.kjs990114.goodong.application.service.user;

import com.kjs990114.goodong.application.dto.UserInfoDTO;
import com.kjs990114.goodong.application.port.in.user.GetUserInfoUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class LoadUserInfoService implements GetUserInfoUseCase {

    private final LoadUserPort loadUserPort;
    @Value("${spring.cloud.gcp.storage.path}")
    private String storagePath;
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Transactional(readOnly = true)
    @Override
    public UserInfoDTO getUserInfo(LoadUserInfoQuery loadUserInfoQuery) {
        String url = storagePath + bucketName + "/";
        UserInfoDTO response = loadUserPort.loadUserInfoByUserIdBasedOnViewerId(loadUserInfoQuery.getUserId(), loadUserInfoQuery.getViewerId());
        response.setProfileImage(url + response.getProfileImage());
        return response;
    }
}
