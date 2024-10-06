package com.kjs990114.goodong.application.port.in.post;

import com.kjs990114.goodong.application.dto.PostSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetLikedPostsUseCase {

    Page<PostSummaryDTO> getLikedPosts(LoadLikedPostsQuery loadLikedPostsQuery);

    @Getter
    @AllArgsConstructor
    class LoadLikedPostsQuery{
        private Long likerId;
        private Long viewerId;
        private Pageable pageable;
    }
}
