package com.kjs990114.goodong.application.port.in.social;


import lombok.AllArgsConstructor;
import lombok.Getter;

public interface AddFollowUseCase {
    void addFollow(AddFollowCommand addFollowCommand);

    @Getter
    @AllArgsConstructor
    class AddFollowCommand{
        private Long followerId;
        private Long followeeId;
    }
}
