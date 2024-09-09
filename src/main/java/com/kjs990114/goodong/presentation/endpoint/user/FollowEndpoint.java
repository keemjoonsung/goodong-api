package com.kjs990114.goodong.presentation.endpoint.user;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.application.user.FollowService;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowEndpoint {
    private final FollowService followService;
    private final UserAuthService userAuthService;

    //팔로우
    @PostMapping
    public CommonResponseEntity<String> followUser(@RequestParam("userId") Long userId,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long followerId = userAuthService.getUserInfo(token).getUserId();
        followService.follow(userId, followerId);
        return new CommonResponseEntity<>("User followed successfully");
    }

    // 언팔로우
    @DeleteMapping()
    public CommonResponseEntity<String> unfollowUser(@RequestParam("userId") Long userId,
                                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long followerId = userAuthService.getUserInfo(token).getUserId();
        followService.unfollow(userId, followerId);
        return new CommonResponseEntity<>("User unfollowed successfully");
    }

    //팔로워 및 팔로잉 목록 조회
    @GetMapping
    public CommonResponseEntity<List<UserDTO.UserSummary>> getFollowInfo(@RequestParam("userId") Long userId,
                                                             @RequestParam("type") FollowType type) {
        if(type == FollowType.FOLLOWING) {
            return new CommonResponseEntity<>(followService.getFollowings(userId));
        }else if(type == FollowType.FOLLOWER) {
            return new CommonResponseEntity<>(followService.getFollowers(userId));
        }
        return new CommonResponseEntity<>(400, "Invalid type parameter");

    }

    @GetMapping("/check")
    CommonResponseEntity<Boolean> isFollowing(@RequestParam("userId") Long userId,
                                                          @RequestHeader(required = false, name = HttpHeaders.AUTHORIZATION) String token) {
        boolean followed = false;
        Long myId = userAuthService.getUserInfo(token).getUserId();
        if(token != null){
            followed = followService.isFollowing(userId, myId);
        }
        return new CommonResponseEntity<>(followed);

    }

    public enum FollowType {
        FOLLOWING,
        FOLLOWER
    }
}
