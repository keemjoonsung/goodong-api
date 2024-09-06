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
    // 내 정보 수정
    @PatchMapping
    public CommonResponseEntity<Void> updateUserProfile(@RequestParam("userId") Long userId,
                                                    @RequestBody UserDTO.Update update,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if(!userAuthService.getUserInfo(token).getUserId().equals(userId)) {
            throw new GlobalException("User authorization failed");
        }
        userService.updateUser(userId, update);
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
