package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.adapter.out.persistence.mysql.PostPersistenceAdapter;
import com.kjs990114.goodong.application.port.in.post.UpdatePostUseCase;
import com.kjs990114.goodong.application.port.out.gcp.StoreFilePort;
import com.kjs990114.goodong.application.port.out.mysql.LoadPostPort;
import com.kjs990114.goodong.application.port.out.mysql.SavePostPort;
import com.kjs990114.goodong.domain.post.Model;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PostUpdateService implements UpdatePostUseCase {
    private final LoadPostPort loadPostPort;
    private final SavePostPort savePostPort;
    private final StoreFilePort storeFilePort;
    @Override
    public void updatePost(UpdatePostCommand updatePostCommand) throws IOException {
        Post post = loadPostPort.loadByPostId(updatePostCommand.getPostId());
        post.updateTitle(updatePostCommand.getTitle());
        post.updateContent(updatePostCommand.getContent());
        post.updateStatus(updatePostCommand.getStatus());
        String fileName = storeFilePort.storeGlbFile(updatePostCommand.getFile());
        post.addModel(fileName, updatePostCommand.getCommitMessage());
        savePostPort.save(post);
    }
}
