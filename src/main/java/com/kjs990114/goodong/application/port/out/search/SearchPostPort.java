package com.kjs990114.goodong.application.port.out.search;

import com.kjs990114.goodong.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchPostPort {

    Page<Post> searchPost(String query, Pageable pageable);

}
