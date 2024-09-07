package com.kjs990114.goodong.presentation.endpoint;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.application.user.UserService;
import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserEndpoint {

    private final UserAuthService userAuthService;
    private final UserService userService;

    // 정보 반환
    @GetMapping("/{userId}")
    public CommonResponseEntity<UserDTO.UserDetail> getUserProfile(@PathVariable("userId") Long userId) {
        return new CommonResponseEntity<>(userService.getUserInfo(userId));
    }
    // 닉네임 혹은 프로필 이미지 변경
    @PatchMapping("/{userId}")
    public CommonResponseEntity<Void> updateUserProfile(@PathVariable("userId") Long userId,
                                                    @RequestParam(required = false) String nickname,
                                                    @RequestParam(required = false) String profileImageUrl,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if(!userAuthService.getUserInfo(token).getUserId().equals(userId)) {
            throw new GlobalException("User authorization failed");
        }
        if(nickname == null && profileImageUrl == null){
            throw new GlobalException("Nickname and profileImageUrl are required");
        }
        if(nickname != null && !nickname.isEmpty()) {
            userService.updateUserNickname(userId,nickname);
        }
        if(profileImageUrl != null && !profileImageUrl.isEmpty()) {
            userService.updateProfileImage(userId, profileImageUrl);
        }
        return new CommonResponseEntity<>("User profile updated successfully");
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public CommonResponseEntity<Void> deleteUserAccount(@PathVariable("userId") Long userId,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if(!userAuthService.getUserInfo(token).getUserId().equals(userId)) {
            throw new GlobalException("User authorization failed");
        }
        userService.deleteUser(userId);
        return new CommonResponseEntity<>("User account deleted successfully");
    }


}
