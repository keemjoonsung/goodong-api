package com.kjs990114.goodong.application.port.in.social;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface DeleteCommentUseCase {

    void deleteComment(DeleteCommentCommand deleteCommentCommand);

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class DeleteCommentCommand{
        private Long commentId;
        private Long userId;
    }
}
