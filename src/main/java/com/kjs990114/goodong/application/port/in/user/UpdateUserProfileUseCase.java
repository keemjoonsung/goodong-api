package com.kjs990114.goodong.application.port.in.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UpdateUserProfileUseCase {

    String updateUserProfile(UpdateUserProfileCommand updateUserProfileCommand) throws IOException;

    @Getter
    @AllArgsConstructor
    class UpdateUserProfileCommand{
        private Long userId;
        private MultipartFile filePng;
        private String nickname;
    }
}
