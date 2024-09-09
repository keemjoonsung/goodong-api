package com.kjs990114.goodong.application.user;

import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.domain.user.Contribution;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public UserDTO.UserDetail getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        int followingCount = user.getFollowings().size();
        int followerCount = user.getFollowers().size();

        return UserDTO.UserDetail.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void updateUserNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        user.updateNickname(nickname);
        userRepository.save(user);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void updateProfileImage(Long userId,String profileImageUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        user.updateProfileImage(profileImageUrl);
        userRepository.save(user);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "contributions", key = "#userId")
    public List<UserDTO.UserContribution> getContributionList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        return user.getContributions().stream().map(
                contribution -> new UserDTO.UserContribution(contribution.getDate(), contribution.getCount())
        ).toList();
    }

}



