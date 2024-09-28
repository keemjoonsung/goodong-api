package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.adapter.in.web.dto.PostDTO;
import com.kjs990114.goodong.application.service.UserAuthService;
import com.kjs990114.goodong.application.service.FollowService;
import com.kjs990114.goodong.application.service.UserService;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.adapter.in.web.dto.ApiResponse;
import com.kjs990114.goodong.adapter.in.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserEndpoint {

    private final UserAuthService userAuthService;
    private final UserService userService;
    private final FollowService followService;


    // 정보 반환
    @GetMapping("/{userId}")
    public ApiResponse<UserDTO.UserDetail> getUserProfile(@PathVariable("userId") Long userId,
                                                          @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        Long viewerId = token == null ? null : userAuthService.getUserId(token);
        UserDTO.UserDetail userDetail = userService.getUserInfoDetail(userId);
        if (viewerId != null) {
            userDetail.setFollowed(followService.isFollowing(userId, viewerId));
        }
        userDetail.setFollowerCount(followService.getFollowerCount(userId));
        userDetail.setFollowingCount(followService.getFollowingCount(userId));
        return new ApiResponse<>(userDetail);
    }

    // 닉네임 혹은 프로필 이미지 변경
    @PatchMapping("/{userId}")
    public ApiResponse<Void> updateUserProfile(@PathVariable("userId") Long userId,
                                               UserDTO.UpdateUser update,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {

        if (!userAuthService.getUserId(token).equals(userId)) {
            throw new UnAuthorizedException("User authorization failed");
        }
        if(update.getNickname() != null && update.getProfileImage() != null) {
            userService.updateUserProfile(userId, update);
        }
        return new ApiResponse<>("User profile updated successfully");
    }


    @GetMapping("/{userId}/contributions")
    public ApiResponse<List<UserDTO.UserContribution>> getContributionList(
            @PathVariable("userId") Long userId
    ) {
        return new ApiResponse<>(userService.getContributionList(userId));
    }

    //팔로우
    @PostMapping("/follows")
    public ApiResponse<String> followUser(@RequestParam("userId") Long userId,
                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long followerId = userAuthService.getUserId(token);
        followService.follow(userId, followerId);
        return new ApiResponse<>("User followed successfully");
    }

    // 언팔로우
    @DeleteMapping("/follows")
    public ApiResponse<String> unfollowUser(@RequestParam("userId") Long userId,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long followerId = userAuthService.getUserId(token);
        followService.unfollow(userId, followerId);
        return new ApiResponse<>("User unfollowed successfully");
    }

    //팔로워 및 팔로잉 목록 조회
    @GetMapping("/follows")
    public ApiResponse<List<UserDTO.UserSummary>> getFollowInfo(@RequestParam("userId") Long userId,
                                                                @RequestParam("type") FollowType type) {
        if(type == FollowType.FOLLOWING) {
            return new ApiResponse<>(followService.getFollowings(userId));
        }else if(type == FollowType.FOLLOWER) {
            return new ApiResponse<>(followService.getFollowers(userId));
        }
        return new ApiResponse<>(400, "Invalid type parameter");

    }

    //좋아요 추가
    @PostMapping("/likes")
    public ApiResponse<Void> likePost(@RequestParam("postId") Long postId,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long likerId = userAuthService.getUserId(token);
        likeService.likePost(postId, likerId);
        return new ApiResponse<>("Like successfully");
    }

    //좋아요 취소
    @DeleteMapping("/likes")
    public ApiResponse<Void> unlikePost(@RequestParam("postId") Long postId,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long likerId = userAuthService.getUserId(token);
        likeService.unlikePost(postId, likerId);
        return new ApiResponse<>("Unlike successfully");
    }
    //좋아요 목록 반환
    @GetMapping("/likes")
    public ApiResponse<List<PostDTO.Summary>> getLikedPosts(@RequestParam Long userId){
        return new ApiResponse<>(likeService.getLikedPosts(userId));
    }
    public enum FollowType {
        FOLLOWING,
        FOLLOWER
    }


}
