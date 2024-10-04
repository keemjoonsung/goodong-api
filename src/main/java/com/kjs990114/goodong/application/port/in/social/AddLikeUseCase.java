package com.kjs990114.goodong.application.port.in.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface AddLikeUseCase {

    void addLike(AddLikeCommand addLikeCommand);

    @Getter
    @AllArgsConstructor
    class AddLikeCommand{
        private Long postId;
        private Long userId;
    }
}
