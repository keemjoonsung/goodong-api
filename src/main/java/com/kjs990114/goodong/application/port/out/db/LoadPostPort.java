package com.kjs990114.goodong.application.port.out.db;

import com.kjs990114.goodong.application.dto.PostDTO;
import com.kjs990114.goodong.application.dto.PostDTO.PostDetailDTO;
import com.kjs990114.goodong.application.dto.PostSummaryDTO;
import com.kjs990114.goodong.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoadPostPort {
    Post loadByPostIdAndUserId(Long postId, Long userId);
    boolean existsByTitleAndUserId(String title, Long userId);
    PostDetailDTO loadDetailByPostIdBasedOnViewerId(Long postId, Long viewerId);
    List<Post> loadByPostIds(List<Long> postIds);
    Page<PostSummaryDTO> loadPageByUserIdBasedOnViewerId(Long userId, Long viewerId, Pageable pageable);
    Page<PostSummaryDTO> loadPageByLikerIdBasedOnViewerId(Long likerId, Long viewerId, Pageable pageable);
    boolean existsByUserIdAndFileName(Long userId, String fileName);
}
