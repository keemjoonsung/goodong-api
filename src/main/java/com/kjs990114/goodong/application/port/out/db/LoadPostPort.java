package com.kjs990114.goodong.application.port.out.db;

import com.kjs990114.goodong.domain.post.Post;

public interface LoadPostPort {
    Post loadByPostIdAndUserId(Long postId, Long userId);
    Post loadByPostId(Long postId);
}
