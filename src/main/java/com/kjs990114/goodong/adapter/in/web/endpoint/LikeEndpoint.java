package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.adapter.in.web.ApiResponse;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase.TokenQuery;
import com.kjs990114.goodong.application.port.in.social.AddLikeUseCase;
import com.kjs990114.goodong.application.port.in.social.AddLikeUseCase.AddLikeCommand;
import com.kjs990114.goodong.application.port.in.social.DeleteLikeUseCase;
import com.kjs990114.goodong.application.port.in.social.DeleteLikeUseCase.DeleteLikeCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeEndpoint {

    private final CheckTokenUseCase checkTokenUseCase;
    private final AddLikeUseCase addLikeUseCase;
    private final DeleteLikeUseCase deleteLikeUseCase;

    //좋아요 추가
    @PostMapping
    public ApiResponse<Void> likePost(@RequestParam("postId") Long postId,
                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        addLikeUseCase.addLike(new AddLikeCommand(postId,userId));
        return new ApiResponse<>("Like successfully");
    }

    //좋아요 취소
    @DeleteMapping
    public ApiResponse<Void> unlikePost(@RequestParam("postId") Long postId,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        deleteLikeUseCase.deleteLike(new DeleteLikeCommand(postId,userId));
        return new ApiResponse<>("Unlike successfully");
    }



}
