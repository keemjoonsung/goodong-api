package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.port.in.post.CreatePostUseCase;
import com.kjs990114.goodong.application.port.out.LoadUserPort;
import com.kjs990114.goodong.application.port.out.SavePostPort;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PostCreationService implements CreatePostUseCase {

    private final SavePostPort savePostPort;

    @Transactional
    @Override
    public void createPost(CreatePostCommand createPostCommand) {
        Post newPost = new Post();
        newPost.updateTitle(createPostCommand.getTitle());
        newPost.updateContent(createPostCommand.getContent());
        newPost.updateStatus(createPostCommand.getStatus());
        newPost.updateOwner(User.of(createPostCommand.getUserId()));
        savePostPort.save(newPost);
    }
}
