package com.kjs990114.goodong.application.service.social;

import com.kjs990114.goodong.application.port.in.social.AddCommentUseCase;
import com.kjs990114.goodong.application.port.out.db.SaveCommentPort;
import com.kjs990114.goodong.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddCommentService implements AddCommentUseCase {

    private final SaveCommentPort saveCommentPort;

    @Override
    public void addComment(AddCommentCommand addCommentCommand) {
        Comment comment = Comment.of(addCommentCommand.getUserId(), addCommentCommand.getPostId());
        comment.updateContent(addCommentCommand.getContent());
        saveCommentPort.save(comment);
    }
}
