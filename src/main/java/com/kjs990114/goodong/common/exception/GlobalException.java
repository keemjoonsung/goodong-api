package com.kjs990114.goodong.common.exception;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GlobalException extends RuntimeException{
    private String message;
}
