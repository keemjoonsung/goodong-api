package com.kjs990114.goodong.application.port.out.db;

public interface DeleteCommentPort {
    void delete(Long commentId, Long userId);
}