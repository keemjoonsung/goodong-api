package com.kjs990114.goodong.adapter.in.web.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private final Integer status;
    private final String message;
    private final T data;

    public ApiResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
    public ApiResponse(String message) {
        this.status = HttpStatus.OK.value();
        this.message = message;
        this.data = null;
    }
    public ApiResponse(T data) {
        this.status = HttpStatus.OK.value();
        this.message = "";
        this.data = data;
    }
    public ApiResponse(String message, T data) {
        this.status = HttpStatus.OK.value();
        this.message = message;
        this.data = data;
    }
    public ApiResponse(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}