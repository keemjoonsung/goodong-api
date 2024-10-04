package com.kjs990114.goodong.application.port.out.db;

import com.kjs990114.goodong.domain.comment.Comment;

public interface SaveCommentPort {
    Long save(Comment comment);
}
