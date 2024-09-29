package com.kjs990114.goodong.application.port.in.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface WithDrawUseCase {
    void withdraw(WithDrawCommand withDrawCommand);

    @Getter
    @AllArgsConstructor
    class WithDrawCommand{
        Long userId;
    }
}
