package com.kjs990114.goodong.application.port.in.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface DeleteLikeUseCase {

    void deleteLike(DeleteLikeCommand deleteLikeCommand);

    @Getter
    @AllArgsConstructor
    class DeleteLikeCommand{
        private Long postId;
        private Long userId;
    }
}
