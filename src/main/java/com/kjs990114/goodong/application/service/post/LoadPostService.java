package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.dto.PostDTO.PostDetailDTO;
import com.kjs990114.goodong.application.dto.PostSummaryDTO;
import com.kjs990114.goodong.application.port.in.post.CheckDuplicatePostTitleUseCase;
import com.kjs990114.goodong.application.port.in.post.GetLikedPostsUseCase;
import com.kjs990114.goodong.application.port.in.post.GetPostDetailUseCase;
import com.kjs990114.goodong.application.port.in.post.GetUserPostsUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadCommentPort;
import com.kjs990114.goodong.application.port.out.db.LoadPostPort;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoadPostService implements GetPostDetailUseCase, GetUserPostsUseCase, GetLikedPostsUseCase , CheckDuplicatePostTitleUseCase {

    private final LoadPostPort loadPostPort;
    private final LoadCommentPort loadCommentPort;
    @Value("${spring.cloud.gcp.storage.path}")
    private String storagePath;
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Transactional(readOnly = true)
    @Override
    public PostDetailDTO getPostDetail(LoadPostDetailCommand loadPostDetailCommand) {
        Long postId = loadPostDetailCommand.getPostId();
        Long viewerId = loadPostDetailCommand.getViewerId();
        PostDetailDTO postDetailDTO = loadPostPort.loadDetailByPostIdBasedOnViewerId(postId,viewerId);
        if(postDetailDTO.getStatus().equals(Post.PostStatus.PRIVATE) && !postDetailDTO.getUserId().equals(viewerId)){
            throw new UnAuthorizedException("UnAuthorized for post");
        }
        String url = storagePath + bucketName + "/";
        postDetailDTO.getModels().forEach(modelInfoDTO -> modelInfoDTO.setUrl(url + modelInfoDTO.getUrl()));
        return postDetailDTO;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostSummaryDTO> getUserPosts(LoadPostsByPageCommand loadPostsByPageCommand) {
        Long userId = loadPostsByPageCommand.getUserId();
        Long viewerId = loadPostsByPageCommand.getViewerId();
        Pageable pageable = loadPostsByPageCommand.getPageable();
        return loadPostPort.loadPageByUserIdBasedOnViewerId(userId, viewerId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PostSummaryDTO> getLikedPosts(LoadLikedPostsQuery loadLikedPostsQuery) {
        Long likerId = loadLikedPostsQuery.getLikerId();
        Long viewerId = loadLikedPostsQuery.getViewerId();
        Pageable pageable = loadLikedPostsQuery.getPageable();
        return loadPostPort.loadPageByLikerIdBasedOnViewerId(likerId,viewerId,pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean checkTitle(CheckPostTitleQuery checkPostTitleQuery) {
        return loadPostPort.existsByTitleAndUserId(checkPostTitleQuery.getTitle(), checkPostTitleQuery.getUserId());
    }
}
