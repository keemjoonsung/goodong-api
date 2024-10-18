package com.kjs990114.goodong.application.service.social;

import com.kjs990114.goodong.application.port.in.social.UpdateCommentUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadCommentPort;
import com.kjs990114.goodong.application.port.out.db.SaveCommentPort;
import com.kjs990114.goodong.common.exception.Error;
import com.kjs990114.goodong.common.exception.ErrorException;
import com.kjs990114.goodong.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCommentService implements UpdateCommentUseCase {

    private final LoadCommentPort loadCommentPort;
    private final SaveCommentPort saveCommentPort;

    @Override
    public void updateComment(UpdateCommentCommand updateCommentCommand) {
        System.out.println("updateCommentCommand.getCommentId() = " + updateCommentCommand.getCommentId());
        Comment comment = loadCommentPort.loadByCommentId(updateCommentCommand.getCommentId()).orElseThrow(()-> new ErrorException(Error.COMMENT_NOT_FOUND));
        if(!comment.getUserId().equals(updateCommentCommand.getUserId())){
            throw new ErrorException(Error.UNAUTHORIZED_ACCESS);
        }
        comment.updateContent(updateCommentCommand.getContent());
        saveCommentPort.save(comment);
    }
}
