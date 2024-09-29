package com.kjs990114.goodong.adapter.out.persistence;

import com.kjs990114.goodong.application.port.out.SavePostPort;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostPersistenceAdapter implements SavePostPort {
    @Override
    public Post save(Post post) {
        return null;
    }
}
