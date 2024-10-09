package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.adapter.in.web.ApiResponse;
import com.kjs990114.goodong.application.dto.UserDTO.ContributionsDTO;
import com.kjs990114.goodong.application.dto.UserDTO.UpdateUserDTO;
import com.kjs990114.goodong.application.dto.UserDetailDTO;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase.TokenQuery;
import com.kjs990114.goodong.application.port.in.user.GetUserContributionUseCase;
import com.kjs990114.goodong.application.port.in.user.GetUserContributionUseCase.LoadContributionQuery;
import com.kjs990114.goodong.application.port.in.user.GetUserInfoUseCase;
import com.kjs990114.goodong.application.port.in.user.GetUserInfoUseCase.LoadUserInfoQuery;
import com.kjs990114.goodong.application.port.in.user.UpdateUserProfileUseCase;
import com.kjs990114.goodong.application.port.in.user.UpdateUserProfileUseCase.UpdateUserProfileCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserEndpoint {

    private final GetUserContributionUseCase getUserContributionUseCase;
    private final CheckTokenUseCase checkTokenUseCase;
    private final GetUserInfoUseCase getUserInfoUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;

    @GetMapping("/{userId}/contributions")
    public ApiResponse<ContributionsDTO> getContributionList(
            @PathVariable("userId") Long userId
    ) {
        return new ApiResponse<>(getUserContributionUseCase.getContributions(new LoadContributionQuery(userId)));
    }
    // 정보 반환
    @GetMapping("/{userId}")
    public ApiResponse<UserDetailDTO> getUserProfile(@PathVariable("userId") Long userId,
                                                     @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        Long viewerId = token == null ? null : checkTokenUseCase.getUserId(new TokenQuery(token));
        UserDetailDTO response = getUserInfoUseCase.getUserInfo(new LoadUserInfoQuery(userId,viewerId));
        return new ApiResponse<>(response);
    }

    // 닉네임 혹은 프로필 이미지 변경
    @PatchMapping
    public ApiResponse<String> updateUserProfile(UpdateUserDTO update,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long userId = checkTokenUseCase.getUserId(new TokenQuery(token));
        String jwt = updateUserProfileUseCase.updateUserProfile(new UpdateUserProfileCommand(userId,update.getFilePng(), update.getNickname()));
        return new ApiResponse<>("User profile updated successfully",jwt);
    }



}
