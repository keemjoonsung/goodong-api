package com.kjs990114.goodong.application.user;

import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.domain.user.Follow;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import com.kjs990114.goodong.presentation.endpoint.user.FollowEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;

    @Transactional

    public void follow(Long followeeId, Long followerId) {;
        User follower = userRepository.findById(followerId).orElseThrow(() -> new NotFoundException("User does not exists"));
        User followee = userRepository.findById(followeeId).orElseThrow(() -> new NotFoundException("User does not exists"));
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
    public void unfollow(Long followeeId, Long followerId) {
        User follower = userRepository.findById(followerId).orElseThrow(() -> new NotFoundException("User does not exists"));
        User followee = userRepository.findById(followeeId).orElseThrow(() -> new NotFoundException("User does not exists"));
        follower.unfollow(followeeId);
        followee.deleteFollower(followerId);
        userRepository.save(follower);
        userRepository.save(followee);

    }

    @Transactional(readOnly = true)
    public List<UserDTO.UserSummary> getFollowings(Long userId) {
        return getFollow(userId, FollowEndpoint.FollowType.FOLLOWING);
    }

    @Transactional(readOnly = true)
    public List<UserDTO.UserSummary> getFollowers(Long userId) {
        return getFollow(userId, FollowEndpoint.FollowType.FOLLOWER);
    }

    @Transactional(readOnly = true)
    public boolean isFollowing(Long userId, Long myId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        User me = userRepository.findById(myId).orElseThrow(() -> new NotFoundException("User does not exists"));
        return user.getFollowers().stream().anyMatch(follow ->
                follow.getFollower().getUserId().equals(me.getUserId())
        );
    }

    @Transactional(readOnly = true)
    public int getFollowerCount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        return user.getFollowers().size();
    }

    @Transactional(readOnly = true)
    public int getFollowingCount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        return user.getFollowings().size();
    }


    private List<UserDTO.UserSummary> getFollow(Long userId, FollowEndpoint.FollowType type) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
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
