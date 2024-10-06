package com.kjs990114.goodong.application.port.in.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface UpdateCommentUseCase {

    void updateComment(UpdateCommentCommand updateCommentCommand);

    @Getter
    @AllArgsConstructor
    class UpdateCommentCommand{
        private Long commentId;
        private Long userId;
        private String content;
    }
}
