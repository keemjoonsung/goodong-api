package com.kjs990114.goodong.presentation.endpoint.user;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.application.user.FollowService;
import com.kjs990114.goodong.application.user.UserService;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserEndpoint {

    private final UserAuthService userAuthService;
    private final UserService userService;
    private final FollowService followService;


    // 정보 반환
    @GetMapping("/{userId}")
    public CommonResponseEntity<UserDTO.UserDetail> getUserProfile(@PathVariable("userId") Long userId,
                                                                   @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        Long viewerId = token == null ? null : userAuthService.getUserId(token);
        UserDTO.UserDetail userDetail = userService.getUserInfoDetail(userId);
        if (viewerId != null) {
            userDetail.setFollowed(followService.isFollowing(userId, viewerId));
        }
        userDetail.setFollowerCount(followService.getFollowerCount(userId));
        userDetail.setFollowingCount(followService.getFollowingCount(userId));
        return new CommonResponseEntity<>(userDetail);
    }

    // 닉네임 혹은 프로필 이미지 변경
    @PatchMapping("/{userId}")
    public CommonResponseEntity<Void> updateUserProfile(@PathVariable("userId") Long userId,
                                                        UserDTO.UpdateUser update,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {

        if (!userAuthService.getUserId(token).equals(userId)) {
            throw new UnAuthorizedException("User authorization failed");
        }
        if(update.getNickname() != null && update.getProfileImage() != null) {
            userService.updateUserProfile(userId, update);
        }
        return new CommonResponseEntity<>("User profile updated successfully");
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public CommonResponseEntity<Void> deleteUserAccount(@PathVariable("userId") Long userId,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (!userAuthService.getUserId(token).equals(userId)) {
            throw new UnAuthorizedException("User authorization failed");
        }
        userService.deleteUser(userId);
        return new CommonResponseEntity<>("User account deleted successfully");
    }


}
