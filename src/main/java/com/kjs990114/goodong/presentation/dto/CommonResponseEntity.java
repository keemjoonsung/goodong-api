package com.kjs990114.goodong.presentation.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonResponseEntity<T> {
    private final Integer status;
    private final String message;
    private final T data;

    public CommonResponseEntity(Integer status) {
        this.status = status;
        this.message = "";
        this.data = null;
    }
    public CommonResponseEntity(String message) {
        this.status = HttpStatus.OK.value();
        this.message = message;
        this.data = null;
    }
    public CommonResponseEntity(T data) {
        this.status = HttpStatus.OK.value();
        this.message = "";
        this.data = data;
    }
    public CommonResponseEntity(String message, T data) {
        this.status = HttpStatus.OK.value();
        this.message = message;
        this.data = data;
    }
    public CommonResponseEntity(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}