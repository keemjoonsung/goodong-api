package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.adapter.in.web.dto.PostDTO.PostDetailDTO;
import com.kjs990114.goodong.adapter.in.web.dto.PostDTO.PostSummaryDTO;
import com.kjs990114.goodong.application.port.in.post.GetPostsByPageUseCase;
import com.kjs990114.goodong.application.port.in.post.GetPostDetailUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadPostPort;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadPostsDetailService implements GetPostDetailUseCase, GetPostsByPageUseCase {

    private final LoadPostPort loadPostPort;

    @Override
    public PostDetailDTO getPostDetail(LoadPostDetailCommand loadPostDetailCommand) {
        Long postId = loadPostDetailCommand.getPostId();
        Long viewerId = loadPostDetailCommand.getViewerId();
        Post post = loadPostPort.loadByPostId(postId);
        boolean isLiked = post.isLikedBy(viewerId);
        return PostDetailDTO.of(post,isLiked);
    }


    @Override
    public Page<PostSummaryDTO> getPostByPage(LoadPostsByPageCommand loadPostsByPageCommand) {
        Long userId = loadPostsByPageCommand.getUserId();
        Long viewerId = loadPostsByPageCommand.getViewerId();
        Pageable pageable = loadPostsByPageCommand.getPageable();
        Page<Post> page = loadPostPort.loadPageByUserIdBasedOnViewerId(userId,viewerId,pageable);
        return page.map(PostSummaryDTO::of);
    }

}
