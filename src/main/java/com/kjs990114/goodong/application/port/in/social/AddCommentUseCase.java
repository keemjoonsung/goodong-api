package com.kjs990114.goodong.application.port.in.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface AddCommentUseCase {

    void addComment(AddCommentCommand addCommentCommand);

    @Getter
    @AllArgsConstructor
    class AddCommentCommand{
        private Long postId;
        private Long userId;
        private String content;
    }
}
