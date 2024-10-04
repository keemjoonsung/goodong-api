package com.kjs990114.goodong.application.port.in.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface CheckDuplicatePostTitleUseCase {

    boolean checkTitle(CheckPostTitleQuery checkPostTitleQuery);

    @Getter
    @AllArgsConstructor
    class CheckPostTitleQuery{
        private String title;
        private Long userId;
    }
}
