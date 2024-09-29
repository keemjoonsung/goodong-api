package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.port.in.post.DeletePostUseCase;
import com.kjs990114.goodong.application.port.out.db.DeletePostPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeletePostService implements DeletePostUseCase {

    private final DeletePostPort deletePostPort;

    @Transactional
    @Override
    public void deletePost(DeletePostCommand deletePostCommand) {
        Long postId = deletePostCommand.getPostId();
        Long userId = deletePostCommand.getUserId();
        deletePostPort.delete(postId, userId);
    }




}
