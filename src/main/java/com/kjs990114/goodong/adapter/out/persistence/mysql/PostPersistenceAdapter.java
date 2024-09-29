package com.kjs990114.goodong.adapter.out.persistence.mysql;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.PostEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.mapper.PostMapper;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.PostRepository;
import com.kjs990114.goodong.application.port.out.mysql.SavePostPort;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostPersistenceAdapter implements SavePostPort {

    private final PostRepository postRepository;

    @Override
    public Post save(Post post) {
        PostEntity postEntity = PostMapper.toEntity(post);
        postRepository.save(postEntity);
        return new Post();
    }
}
