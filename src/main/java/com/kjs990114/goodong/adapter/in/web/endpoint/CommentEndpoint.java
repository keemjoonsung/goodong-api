package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.application.dto.ApiResponse;
import com.kjs990114.goodong.application.dto.PostDTO.CommentDTO;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase.TokenQuery;
import com.kjs990114.goodong.application.port.in.social.AddCommentUseCase;
import com.kjs990114.goodong.application.port.in.social.DeleteCommentUseCase;
import com.kjs990114.goodong.application.port.in.social.DeleteCommentUseCase.DeleteCommentCommand;
import com.kjs990114.goodong.application.port.in.social.UpdateCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentEndpoint {

    private final CheckTokenUseCase checkTokenUseCase;
    private final AddCommentUseCase addCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;
    private final UpdateCommentUseCase updateCommentUseCase;
    // 댓글 달기
    @PostMapping  // 댓글 달
    public ApiResponse<Void> addComment(@RequestParam("postId") Long postId,
                                        @RequestBody CommentDTO commentDTO,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        addCommentUseCase.addComment(new AddCommentUseCase.AddCommentCommand(postId, userId, commentDTO.getContent()));
        return new ApiResponse<>("Comment added successfully");
    }

    //댓글 삭제 하기
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable("commentId") Long commentId,
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        deleteCommentUseCase.deleteComment(new DeleteCommentCommand(commentId,userId));
        return new ApiResponse<>("Comment deleted successfully");
    }

    //댓글 업데이트 하기
    @PatchMapping("/{commentId}")
    public ApiResponse<Void> updateComment(@PathVariable("commentId") Long commentId,
                                           @RequestBody CommentDTO postComment, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        updateCommentUseCase.updateComment(new UpdateCommentUseCase.UpdateCommentCommand(commentId,userId,postComment.getContent()));
        return new ApiResponse<>("Comment updated successfully");

    }


}
