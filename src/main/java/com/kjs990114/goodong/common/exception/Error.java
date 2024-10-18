package com.kjs990114.goodong.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Error {
    //400
    DUPLICATE_NICKNAME(400,"400_000","중복된 닉네임 입니다."),
    DUPLICATE_TITLE(400 , "400_001", "중복된 제목을 가진 게시글이 존재합니다."),
    INVALID_FILE(404,"404_002","파일이 올바르지 않습니다."),
    //401
    LOGIN_FAILED(401, "401_000", "로그인에 실패하였습니다."),
    UNAUTHORIZED_ACCESS(401, "401_001", "접근 권한이 없습니다."),
    EXPIRED_TOKEN(401, "401_002", "토큰이 만료 되었습니다"),
    INVALID_TOKEN(401,"401_003","토큰이 유효하지 않습니다."),
    WRONG_TOKEN(401, "401_004", "잘못된 토큰 입니다."),
    //404
    POST_NOT_FOUND(404,"404_000", "해당 게시글을 찾을 수 없습니다."),
    USER_NOT_FOUND(404,"404_001", "해당 유저를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(404,"404_002","해당 댓글을 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String msg;
}
