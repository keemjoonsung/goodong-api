package com.kjs990114.goodong.application.service.social;

import com.kjs990114.goodong.application.port.in.social.DeleteCommentUseCase;
import com.kjs990114.goodong.application.port.out.db.DeleteCommentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCommentService implements DeleteCommentUseCase {

    private final DeleteCommentPort deleteCommentPort;

    @Override
    public void deleteComment(DeleteCommentCommand deleteCommentCommand) {
        deleteCommentPort.delete(deleteCommentCommand.getCommentId(), deleteCommentCommand.getUserId());
    }
}
