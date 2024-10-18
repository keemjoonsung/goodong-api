package com.kjs990114.goodong.common.exception;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException {
    private final Error errorCode;
    public ErrorException(Error error) {
        super(error.getMsg());
        this.errorCode = error;
    }
}
