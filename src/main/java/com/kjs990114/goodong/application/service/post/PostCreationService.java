package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.port.in.post.CreatePostUseCase;
import com.kjs990114.goodong.application.port.out.gcp.StoreFilePort;
import com.kjs990114.goodong.application.port.out.mysql.SavePostPort;
import com.kjs990114.goodong.domain.post.Model;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class PostCreationService implements CreatePostUseCase {

    private final SavePostPort savePostPort;
    private final StoreFilePort storeFilePort;
    @Transactional
    @Override
    public void createPost(CreatePostCommand createPostCommand) throws IOException {

        Post newPost = new Post();
        newPost.updateTitle(createPostCommand.getTitle());
        newPost.updateContent(createPostCommand.getContent());
        newPost.updateStatus(createPostCommand.getStatus());
        newPost.updateOwner(User.of(createPostCommand.getUserId()));
        newPost.addTagAll(createPostCommand.getTags());
        String fileName = storeFilePort.storeGlbFile(createPostCommand.getFile());
        Model newModel = Model.builder()
                .version(newPost.getNextModelVersion())
                .commitMessage("First Commit")
                .post(newPost)
                .fileName(fileName)
                .build();
        newPost.addModel(newModel);

        savePostPort.save(newPost);
    }
}
