package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.port.in.post.CreatePostUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadUserPort;
import com.kjs990114.goodong.application.port.out.db.SaveUserPort;
import com.kjs990114.goodong.application.port.out.storage.StoreFilePort;
import com.kjs990114.goodong.application.port.out.db.SavePostPort;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class CreatePostService implements CreatePostUseCase {

    private final SavePostPort savePostPort;
    private final StoreFilePort storeFilePort;
    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    @Transactional
    @Override
    public void createPost(CreatePostCommand createPostCommand) throws IOException {
        Post newPost = Post.of(null, createPostCommand.getUserId());
        User user = loadUserPort.loadByUserId(createPostCommand.getUserId());
        newPost.updateTitle(createPostCommand.getTitle());
        newPost.updateContent(createPostCommand.getContent());
        newPost.updateStatus(createPostCommand.getStatus());
        newPost.updateTag(createPostCommand.getTags());
        user.addContribution();
        String fileName = storeFilePort.storeFile(createPostCommand.getFile());
        if(fileName == null) throw new NotFoundException("File Not Found");
        newPost.addModel(fileName, "First Commit");
        saveUserPort.save(user);
        savePostPort.save(newPost);
    }
}
