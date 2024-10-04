package com.kjs990114.goodong.application.port.out.db;

import com.kjs990114.goodong.application.dto.CommentInfoDTO;
import com.kjs990114.goodong.domain.comment.Comment;

import java.util.List;
import java.util.Optional;

public interface LoadCommentPort {
    List<CommentInfoDTO> loadAllByPostId(Long postId);
    Optional<Comment> loadByCommentId(Long commentId);
}
