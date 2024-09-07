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
    @GetMapping
    public CommonResponseEntity<UserDTO.UserDetail> getUserProfile(@RequestParam("userId") Long userId) {
        return new CommonResponseEntity<>(userService.getUserInfo(userId));
    }
    // 닉네임 변경
    @PatchMapping("/nickname")
    public CommonResponseEntity<Void> updateUserNickname(@RequestParam("userId") Long userId,
                                                    @RequestBody UserDTO.UpdateNickname update,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if(!userAuthService.getUserInfo(token).getUserId().equals(userId)) {
            throw new GlobalException("User authorization failed");
        }
        userService.updateUserNickname(userId, update);
        return new CommonResponseEntity<>("User profile updated successfully");
    }
    //프로필 이미지 변경
    @PatchMapping("/profileImage")
    public CommonResponseEntity<Void> updateUserProfileImage(@RequestParam("userId") Long userId,
                                                        @RequestBody UserDTO.UpdateProfileImage update,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if(!userAuthService.getUserInfo(token).getUserId().equals(userId)) {
            throw new GlobalException("User authorization failed");
        }
        userService.updateProfileImage(userId, update);
        return new CommonResponseEntity<>("User profile updated successfully");
    }

    // 회원 탈퇴
    @DeleteMapping
    public CommonResponseEntity<Void> deleteUserAccount(@RequestParam("userId") Long userId,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if(!userAuthService.getUserInfo(token).getUserId().equals(userId)) {
            throw new GlobalException("User authorization failed");
        }
        userService.deleteUser(userId);
        return new CommonResponseEntity<>("User account deleted successfully");
    }


}
