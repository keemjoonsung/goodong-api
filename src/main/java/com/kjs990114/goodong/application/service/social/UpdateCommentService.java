package com.kjs990114.goodong.application.service.social;

import com.kjs990114.goodong.application.port.in.social.UpdateCommentUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadCommentPort;
import com.kjs990114.goodong.application.port.out.db.SaveCommentPort;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCommentService implements UpdateCommentUseCase {

    private final LoadCommentPort loadCommentPort;
    private final SaveCommentPort saveCommentPort;

    @Override
    public Long updateComment(UpdateCommentCommand updateCommentCommand) {
        Comment comment = loadCommentPort.loadByCommentId(updateCommentCommand.getCommentId()).orElseThrow(()-> new NotFoundException("Comment not Found"));
        if(!comment.getUserId().equals(updateCommentCommand.getUserId())){
            throw new UnAuthorizedException("Not Authorized for comment");
        }
        comment.updateContent(updateCommentCommand.getContent());
        return saveCommentPort.save(comment);
    }
}
