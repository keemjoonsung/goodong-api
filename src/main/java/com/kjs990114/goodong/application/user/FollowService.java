package com.kjs990114.goodong.application.user;

import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.domain.user.Follow;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.UserRepository;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import com.kjs990114.goodong.presentation.endpoint.user.FollowEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final RedisTemplate<String,Object> redisTemplate;

    @Transactional

    public void follow(Long followeeId, Long followerId) {;
        User follower = userRepository.findByUserId(followerId).orElseThrow(() -> new NotFoundException("User does not exists"));
        User followee = userRepository.findByUserId(followeeId).orElseThrow(() -> new NotFoundException("User does not exists"));
        Follow follow = Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();

        follower.follow(follow);
        followee.addFollower(follow);
        userRepository.save(follower);
        userRepository.save(followee);
        redisTemplate.delete("followerCount:" + followeeId);
        redisTemplate.delete("followingCount:" + followerId);
    }

    @Transactional
    public void unfollow(Long followeeId, Long followerId) {
        User follower = userRepository.findByUserId(followerId).orElseThrow(() -> new NotFoundException("User does not exists"));
        User followee = userRepository.findByUserId(followeeId).orElseThrow(() -> new NotFoundException("User does not exists"));
        follower.unfollow(followeeId);
        followee.deleteFollower(followerId);
        userRepository.save(follower);
        userRepository.save(followee);
        redisTemplate.delete("followerCount:" + followeeId);
        redisTemplate.delete("followingCount:" + followerId);
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
        if(userId.equals(myId)) return false;
        System.out.println("db접근");
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        User me = userRepository.findByUserId(myId).orElseThrow(() -> new NotFoundException("User does not exists"));

        return user.getFollowers().stream().anyMatch(follow ->
                follow.getFollower().getUserId().equals(me.getUserId())
        );
    }

    @Transactional(readOnly = true)
    public int getFollowerCount(Long userId) {
        ValueOperations<String,Object> valueOps = redisTemplate.opsForValue();
        String key = "followerCount:" + userId;
        Integer followerCount = (Integer) valueOps.get(key);
        if(followerCount != null){
            return followerCount;
        }
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        int count = user.getFollowers().size();
        valueOps.set(key,count);
        return count;
    }

    @Transactional(readOnly = true)
    public int getFollowingCount(Long userId) {
        ValueOperations<String,Object> valueOps = redisTemplate.opsForValue();
        String key = "followingCount:" + userId;
        Integer followingCount = (Integer) valueOps.get(key);
        if(followingCount != null){
            return followingCount;
        }
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        int count = user.getFollowings().size();
        valueOps.set(key,count);
        return count;
    }


    private List<UserDTO.UserSummary> getFollow(Long userId, FollowEndpoint.FollowType type) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
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
