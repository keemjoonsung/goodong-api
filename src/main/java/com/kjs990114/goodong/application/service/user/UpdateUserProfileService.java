package com.kjs990114.goodong.application.service.user;

import com.kjs990114.goodong.application.port.in.user.UpdateUserProfileUseCase;
import com.kjs990114.goodong.application.port.out.cache.DeleteUserCachePort;
import com.kjs990114.goodong.application.port.out.db.LoadUserPort;
import com.kjs990114.goodong.application.port.out.db.SaveUserPort;
import com.kjs990114.goodong.application.port.out.storage.StoreFilePort;
import com.kjs990114.goodong.common.jwt.JwtUtil;
import com.kjs990114.goodong.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Repository
@RequiredArgsConstructor
public class UpdateUserProfileService implements UpdateUserProfileUseCase {
    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    private final StoreFilePort storeFilePort;
    private final JwtUtil jwtUtil;
    private final DeleteUserCachePort deleteUserCachePort;
    @Transactional
    @Override
    public String updateUserProfile(UpdateUserProfileCommand updateUserProfileCommand) throws IOException {
        User user = loadUserPort.loadByUserId(updateUserProfileCommand.getUserId());
        String fileName = storeFilePort.storeFile(updateUserProfileCommand.getFilePng());
        user.changeNickname(updateUserProfileCommand.getNickname());
        user.changeProfileImage(fileName);
        saveUserPort.save(user);
        deleteUserCachePort.deleteUserDTO(user.getUserId());
        return jwtUtil.createJwt(user.getUserId());
    }
}
