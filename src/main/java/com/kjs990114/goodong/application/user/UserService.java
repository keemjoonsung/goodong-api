package com.kjs990114.goodong.application.user;

import com.kjs990114.goodong.application.file.FileService;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.UserRepository;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileService fileService;
    @Value("${spring.cloud.gcp.storage.path}")
    private String storagePath;

    @Transactional(readOnly = true)
    public UserDTO.UserDetail getUserInfo(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));

        return UserDTO.UserDetail.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }


    @Transactional
    public void updateUserProfile(Long userId, UserDTO.UpdateUser update) throws IOException {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        if (update.getNickname() != null) {
            updateUserNickname(user, update.getNickname());
        }
        if (update.getProfileImage() != null) {
            MultipartFile file = update.getProfileImage();
            String fileName = fileService.saveFileStorage(file, FileService.Extension.PNG);
            String profileImageUrl = storagePath + fileName;
            updateProfileImage(user, profileImageUrl);
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        user.softDelete();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO.UserContribution> getContributionList(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        return user.getContributions().stream().map(
                contribution -> new UserDTO.UserContribution(contribution.getDate(), contribution.getCount())
        ).toList();
    }

    private void updateProfileImage(User user, String profileImageUrl) {
        user.updateProfileImage(profileImageUrl);
    }

    private void updateUserNickname(User user, String nickname) {
        user.updateNickname(nickname);
    }

}



