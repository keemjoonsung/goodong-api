package com.kjs990114.goodong.application.port.out.db;

import com.kjs990114.goodong.application.dto.CommentInfoDTO;

import java.util.List;

public interface LoadCommentPort {
    List<CommentInfoDTO> loadAllByPostId(Long postId);
}
