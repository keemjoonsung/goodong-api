package com.kjs990114.goodong.adapter.out.persistence.mysql;

import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.CommentRepository;
import com.kjs990114.goodong.application.dto.CommentInfoDTO;
import com.kjs990114.goodong.application.port.out.db.LoadCommentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements LoadCommentPort {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentInfoDTO> loadAllByPostId(Long postId) {
        return commentRepository.getCommentInfoDTOByPostId(postId);
    }
}
