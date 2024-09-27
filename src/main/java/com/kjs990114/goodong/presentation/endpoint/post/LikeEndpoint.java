package com.kjs990114.goodong.presentation.endpoint.post;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.application.post.LikeService;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeEndpoint {
    private final LikeService likeService;
    private final UserAuthService userAuthService;

    //좋아요 추가
    @PostMapping
    public CommonResponseEntity<Void> likePost(@RequestParam("postId") Long postId,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long likerId = userAuthService.getUserId(token);
        likeService.likePost(postId, likerId);
        return new CommonResponseEntity<>("Like successfully");
    }

    //좋아요 취소
    @DeleteMapping
    public CommonResponseEntity<Void> unlikePost(@RequestParam("postId") Long postId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long likerId = userAuthService.getUserId(token);
        likeService.unlikePost(postId, likerId);
        return new CommonResponseEntity<>("Unlike successfully");
    }
    //좋아요 목록 반환
    @GetMapping
    public CommonResponseEntity<List<PostDTO.Summary>> getLikedPosts(@RequestParam Long userId){
        return new CommonResponseEntity<>(likeService.getLikedPosts(userId));
    }

}
