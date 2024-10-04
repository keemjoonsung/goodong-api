package com.kjs990114.goodong.application.service.user;

import com.kjs990114.goodong.application.port.in.user.UpdateUserProfileUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadUserPort;
import com.kjs990114.goodong.application.port.out.db.SaveUserPort;
import com.kjs990114.goodong.application.port.out.storage.StoreFilePort;
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

    @Transactional
    @Override
    public void updateUserProfile(UpdateUserProfileCommand updateUserProfileCommand) throws IOException {
        User user = loadUserPort.loadByUserId(updateUserProfileCommand.getUserId());
        String fileName = storeFilePort.storeFile(updateUserProfileCommand.getFilePng());
        user.updateNickname(updateUserProfileCommand.getNickname());
        user.updateProfileImage(fileName);
        saveUserPort.save(user);
    }
}
