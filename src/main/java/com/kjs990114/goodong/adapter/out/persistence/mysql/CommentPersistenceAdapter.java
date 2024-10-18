package com.kjs990114.goodong.adapter.out.persistence.mysql;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.CommentEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.mapper.CommentMapper;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.CommentRepository;
import com.kjs990114.goodong.application.dto.CommentInfoDTO;
import com.kjs990114.goodong.application.port.out.db.DeleteCommentPort;
import com.kjs990114.goodong.application.port.out.db.LoadCommentPort;
import com.kjs990114.goodong.application.port.out.db.SaveCommentPort;
import com.kjs990114.goodong.common.exception.Error;
import com.kjs990114.goodong.common.exception.ErrorException;
import com.kjs990114.goodong.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements LoadCommentPort, SaveCommentPort, DeleteCommentPort {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentInfoDTO> loadAllByPostId(Long postId) {
        return commentRepository.getCommentInfoDTOByPostId(postId);
    }

    @Override
    public Optional<Comment> loadByCommentId(Long commentId) {
        return commentRepository.findByCommentId(commentId).map(CommentMapper::toDomain);
    }

    @Override
    public Long save(Comment comment) {
        return commentRepository.save(CommentMapper.toEntity(comment)).getCommentId();
    }

    @Override
    public void delete(Long commentId, Long userId) {
        CommentEntity commentEntity = commentRepository.findByCommentIdAndUserId(commentId,userId).orElseThrow(()-> new ErrorException(Error.COMMENT_NOT_FOUND));
        commentEntity.softDelete();
        commentRepository.save(commentEntity);
    }
}
