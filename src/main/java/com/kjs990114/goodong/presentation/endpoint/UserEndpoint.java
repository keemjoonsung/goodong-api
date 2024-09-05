package com.kjs990114.goodong.presentation.endpoint;

import com.kjs990114.goodong.presentation.dto.CommonResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserEndpoint {

    @GetMapping
    public CommonResponseEntity<String> getUserProfile(@RequestParam("userId") Long userId){
        return new CommonResponseEntity<>("유저");
    }
    // 내 정보 반환
    @GetMapping("/me")
    public ResponseEntity<String> getMyProfile(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok("user");
    }
    // 내 정보 수정
    @PatchMapping("/me")
    public ResponseEntity<String> updateMyProfile(@RequestParam("userId") Long userId,
                                                    @RequestParam("username") String username,
                                                    @RequestParam("email") String email,
                                                    @RequestParam("bio") String bio) {
        return ResponseEntity.ok("User profile updated successfully");
    }
    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyAccount(@RequestParam("userId") Long userId,
                                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok("User account deleted successfully");
    }
    //팔로우하기 (POST 메서드)
    @PostMapping("/{userId}/follow")
    public ResponseEntity<String> followUser(@PathVariable("userId") Long userId,
                                             @RequestParam("followerId") Long followerId,
                                             @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok("User followed successfully");
    }

    // 언팔로우하기 (DELETE 메서드)
    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<String> unfollowUser(@PathVariable("userId") Long userId,
                                               @RequestParam("followerId") Long followerId,
                                               @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok("User unfollowed successfully");
    }

    //팔로워 및 팔로잉 목록 조회
    @GetMapping("/{userId}/follow")
    public ResponseEntity<String> getFollowInfo(@PathVariable("userId") Long userId,
                                           @RequestParam("type") FollowType type,
                                           @RequestParam(value = "detail", defaultValue = "true") boolean detail) {
        if (type == FollowType.FOLLOWING) {
            if (detail) {
                // 팔로잉 목록을 반환
                return ResponseEntity.ok("followingList");
            } else {
                // 팔로잉 수를 반환
                return ResponseEntity.ok("followingCount");
            }
        } else if (type == FollowType.FOLLOWER) {
            if (detail) {
                // 팔로워 목록을 반환
                return ResponseEntity.ok("followersList");
            } else {
                // 팔로워 수를 반환
                return ResponseEntity.ok("followersCount");
            }
        }
        return ResponseEntity.badRequest().body("Invalid type");
    }

    public enum FollowType{
        FOLLOWING,
        FOLLOWER
    }

}
