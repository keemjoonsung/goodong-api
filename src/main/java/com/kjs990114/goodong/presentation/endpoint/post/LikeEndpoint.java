package com.kjs990114.goodong.presentation.endpoint.post;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.application.post.LikeService;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeEndpoint {
    private final LikeService likeService;
    private final UserAuthService userAuthService;
    private final JwtUtil jwtUtil;

    //좋아요 추가
    @PostMapping
    public CommonResponseEntity<Void> likePost(@RequestParam("postId") Long postId,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long likerId = userAuthService.getUserInfo(token).getUserId();
        likeService.likePost(postId, likerId);
        return new CommonResponseEntity<>("Like successfully");
    }

    //좋아요 취소
    @DeleteMapping
    public CommonResponseEntity<Void> unlikePost(@RequestParam("postId") Long postId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long likerId = userAuthService.getUserInfo(token).getUserId();
        likeService.unlikePost(postId, likerId);
        return new CommonResponseEntity<>("Unlike successfully");
    }


}
