package com.kjs990114.goodong.application.port.in.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface DeleteFollowUseCase {
    void deleteFollow(DeleteFollowCommand deleteFollowCommand);

    @Getter
    @AllArgsConstructor
    class DeleteFollowCommand{
        private Long followerId;
        private Long followeeId;
    }
}
