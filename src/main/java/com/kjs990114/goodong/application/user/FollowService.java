package com.kjs990114.goodong.application.user;

import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.domain.user.Follow;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import com.kjs990114.goodong.presentation.endpoint.user.FollowEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "isFollowed"),
            @CacheEvict(value = "followingCount", key = "#followerId"),
            @CacheEvict(value = "followerCount", key = "#followeeId"),
            @CacheEvict(value = "followerList", key = "#followeeId"),
            @CacheEvict(value = "followingList", key = "#followerId")
    })
    public void follow(Long followeeId, Long followerId) {;
        User follower = userRepository.findById(followerId).orElseThrow(() -> new GlobalException("User does not exists"));
        User followee = userRepository.findById(followeeId).orElseThrow(() -> new GlobalException("User does not exists"));
        Follow follow = Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();

        follower.follow(follow);
        followee.addFollower(follow);
        userRepository.save(follower);
        userRepository.save(followee);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "isFollowed"),
            @CacheEvict(value = "followingCount", key = "#followerId"),
            @CacheEvict(value = "followerCount", key = "#followeeId"),
            @CacheEvict(value = "followerList", key = "#followeeId"),
            @CacheEvict(value = "followingList", key = "#followerId")
    })
    public void unfollow(Long followeeId, Long followerId) {
        User follower = userRepository.findById(followerId).orElseThrow(() -> new GlobalException("User does not exists"));
        User followee = userRepository.findById(followeeId).orElseThrow(() -> new GlobalException("User does not exists"));
        follower.unfollow(followeeId);
        followee.deleteFollower(followerId);
        userRepository.save(follower);
        userRepository.save(followee);

    }

    @Transactional(readOnly = true)
    @Cacheable(value = "followingList",key = "#userId")
    public List<UserDTO.UserSummary> getFollowings(Long userId) {
        return getFollow(userId, FollowEndpoint.FollowType.FOLLOWING);
    }

    @Cacheable(value = "followerList",key = "#userId")
    @Transactional(readOnly = true)
    public List<UserDTO.UserSummary> getFollowers(Long userId) {
        return getFollow(userId, FollowEndpoint.FollowType.FOLLOWER);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "isFollowed")
    public boolean isFollowing(Long userId, Long myId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        User me = userRepository.findById(myId).orElseThrow(() -> new GlobalException("User does not exists"));
        return user.getFollowers().stream().anyMatch(follow ->
                follow.getFollower().getUserId().equals(me.getUserId())
        );
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "followerCount", key = "#userId")
    public int getFollowerCount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        return user.getFollowers().size();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "followingCount", key = "#userId")
    public int getFollowingCount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        return user.getFollowings().size();
    }


    private List<UserDTO.UserSummary> getFollow(Long userId, FollowEndpoint.FollowType type) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User does not exists"));
        List<UserDTO.UserSummary> response;
        if (type == FollowEndpoint.FollowType.FOLLOWER) { //팔로워
            Set<Follow> followers = user.getFollowers();
            response = followers.stream().map(f -> {
                User follower = f.getFollower();
                return UserDTO.UserSummary.builder()
                        .userId(follower.getUserId())
                        .email(follower.getEmail())
                        .nickname(follower.getNickname())
                        .profileImage(follower.getProfileImage())
                        .build();
            }).toList();
        } else { // 팔로잉
            Set<Follow> followings = user.getFollowings();
            response = followings.stream().map(following -> {
                User followee = following.getFollowee();

                return UserDTO.UserSummary.builder()
                        .userId(followee.getUserId())
                        .email(followee.getEmail())
                        .nickname(followee.getNickname())
                        .profileImage(followee.getProfileImage())
                        .build();
            }).toList();

        }
        return response;
    }
}
