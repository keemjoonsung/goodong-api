package com.kjs990114.goodong.application.port.out.db;

import com.kjs990114.goodong.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoadPostPort {
    Post loadByPostIdAndUserId(Long postId, Long userId);
    Post loadByPostId(Long postId);
    List<Post> loadByPostIds(List<Long> postIds);
    Page<Post> loadPageByUserIdBasedOnViewerId(Long userId, Long viewerId, Pageable pageable);
}
