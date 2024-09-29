package com.kjs990114.goodong.application.port.out.mysql;

import com.kjs990114.goodong.domain.post.Post;

public interface SavePostPort {
    Post save(Post post);
}
