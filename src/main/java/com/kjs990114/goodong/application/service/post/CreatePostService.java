package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.port.in.post.CreatePostUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadUserPort;
import com.kjs990114.goodong.application.port.out.storage.StoreFilePort;
import com.kjs990114.goodong.application.port.out.db.SavePostPort;
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
    @Transactional
    @Override
    public void createPost(CreatePostCommand createPostCommand) throws IOException {
        Post newPost = Post.of(null, createPostCommand.getUserId());
        newPost.updateTitle(createPostCommand.getTitle());
        newPost.updateContent(createPostCommand.getContent());
        newPost.updateStatus(createPostCommand.getStatus());
        newPost.updateTag(createPostCommand.getTags());
        String fileName = storeFilePort.storeFile(createPostCommand.getFile());
        newPost.addModel(fileName, "First Commit");
        savePostPort.save(newPost);
    }
}
