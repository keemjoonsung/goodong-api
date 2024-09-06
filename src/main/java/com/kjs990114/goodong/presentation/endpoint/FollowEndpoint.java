package com.kjs990114.goodong.presentation.endpoint;

import com.kjs990114.goodong.application.user.FollowService;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowEndpoint {
    private final FollowService followService;
    //팔로우하기 (POST 메서드)
    @PostMapping
    public CommonResponseEntity<String> followUser(@RequestParam("userId") Long userId,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return new CommonResponseEntity<>("User followed successfully");
    }

    // 언팔로우하기 (DELETE 메서드)
    @DeleteMapping()
    public CommonResponseEntity<String> unfollowUser(@RequestParam("userId") Long userId,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return new CommonResponseEntity<>("User unfollowed successfully");
    }

    //팔로워 및 팔로잉 목록 조회
    @GetMapping
    public CommonResponseEntity<String> getFollowInfo(@RequestParam("userId") Long userId,
                                                @RequestParam("type") FollowType type) {
        if (type == FollowType.FOLLOWING) {

        } else if (type == FollowType.FOLLOWER) {
        }
        return new CommonResponseEntity<>("Invalid type");
    }

    public enum FollowType{
        FOLLOWING,
        FOLLOWER
    }
}
