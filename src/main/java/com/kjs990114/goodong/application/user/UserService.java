package com.kjs990114.goodong.application.user;

import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO.Detail getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        int followingCount = user.getFollowings().size();
        int followerCount = user.getFollowers().size();

        return UserDTO.Detail.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .contributions(user.getContributions().stream()
                        .map(cont ->
                                new UserDTO.Contribution(cont.getDate(), cont.getCount())
                        ).toList())
                .build();
    }

    public void updateUser(Long userId, UserDTO.Update update) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        user.updateProfile(update.getNickname(), update.getProfileImage());
        userRepository.save(user);

    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        userRepository.delete(user);
    }


}



