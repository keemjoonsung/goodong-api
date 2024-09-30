package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.port.in.post.UpdatePostUseCase;
import com.kjs990114.goodong.application.port.out.storage.StoreFilePort;
import com.kjs990114.goodong.application.port.out.db.LoadPostPort;
import com.kjs990114.goodong.application.port.out.db.SavePostPort;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UpdatePostService implements UpdatePostUseCase {
    private final LoadPostPort loadPostPort;
    private final SavePostPort savePostPort;
    private final StoreFilePort storeFilePort;
    @Override
    public void updatePost(UpdatePostCommand updatePostCommand) throws IOException {
        Post post = loadPostPort.loadByPostIdAndUserId(updatePostCommand.getPostId(), updatePostCommand.getUserId());
        post.updateTitle(updatePostCommand.getTitle());
        post.updateContent(updatePostCommand.getContent());
        post.updateStatus(updatePostCommand.getStatus());
        post.updateTag(updatePostCommand.getTags());
        String fileName = storeFilePort.storeFile(updatePostCommand.getFile());
        post.addModel(fileName, updatePostCommand.getCommitMessage());
        savePostPort.save(post);
    }
}
