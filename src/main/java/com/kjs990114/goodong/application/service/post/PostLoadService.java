package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.adapter.in.web.dto.PostDTO.PostDetailDTO;
import com.kjs990114.goodong.application.port.in.post.GetPostUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadPostPort;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLoadService implements GetPostUseCase {

    private final LoadPostPort loadPostPort;

    @Override
    public PostDetailDTO getPostDetail(LoadPostDetailCommand loadPostDetailCommand) {
        Long postId = loadPostDetailCommand.getPostId();
        Long viewerId = loadPostDetailCommand.getViewerId();
        Post post = loadPostPort.loadByPostId(postId);
        boolean isLiked = post.isLikedBy(viewerId);
        return PostDetailDTO.of(post,isLiked);
    }
}
