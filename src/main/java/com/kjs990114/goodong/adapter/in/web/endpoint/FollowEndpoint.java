package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.adapter.in.web.ApiResponse;
import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase.TokenQuery;
import com.kjs990114.goodong.application.port.in.social.AddFollowUseCase;
import com.kjs990114.goodong.application.port.in.social.AddFollowUseCase.AddFollowCommand;
import com.kjs990114.goodong.application.port.in.social.DeleteFollowUseCase;
import com.kjs990114.goodong.application.port.in.social.DeleteFollowUseCase.DeleteFollowCommand;
import com.kjs990114.goodong.application.port.in.social.GetFollowersUseCase;
import com.kjs990114.goodong.application.port.in.social.GetFollowersUseCase.GetFollowersQuery;
import com.kjs990114.goodong.application.port.in.social.GetFollowingsUseCase;
import com.kjs990114.goodong.application.port.in.social.GetFollowingsUseCase.GetFollowingsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowEndpoint {

    private final CheckTokenUseCase checkTokenUseCase;
    private final AddFollowUseCase addFollowUseCase;
    private final DeleteFollowUseCase deleteFollowUseCase;
    private final GetFollowersUseCase getFollowersUseCase;
    private final GetFollowingsUseCase getFollowingsUseCase;
    @Value("${spring.page.size}")
    private int pageSize;
    //팔로우
    @PostMapping
    public ApiResponse<String> followUser(@RequestParam("userId") Long followeeId,
                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long followerId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        addFollowUseCase.addFollow(new AddFollowCommand(followerId,followeeId));
        return new ApiResponse<>("User followed successfully");
    }
    //언팔로우
    @DeleteMapping
    public ApiResponse<String> unfollowUser(@RequestParam("userId") Long followeeId,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long followerId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        deleteFollowUseCase.deleteFollow(new DeleteFollowCommand(followerId,followeeId));
        return new ApiResponse<>("User unfollowed successfully");
    }

    //팔로워 및 팔로잉 목록 조회
    @GetMapping
    public ApiResponse<Page<UserSummaryDTO>> getFollowInfo(@RequestParam("userId") Long userId,
                                                           @RequestParam("type") FollowType type,
                                                           @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if(type == FollowType.FOLLOWING) {
            return new ApiResponse<>(getFollowingsUseCase.getFollowings(new GetFollowingsQuery(userId, pageable)));
        }else if(type == FollowType.FOLLOWER) {
            return new ApiResponse<>(getFollowersUseCase.getFollowers(new GetFollowersQuery(userId, pageable)));
        }
        return new ApiResponse<>(400, "Invalid type parameter");

    }
    public enum FollowType {
        FOLLOWING,
        FOLLOWER
    }
}
