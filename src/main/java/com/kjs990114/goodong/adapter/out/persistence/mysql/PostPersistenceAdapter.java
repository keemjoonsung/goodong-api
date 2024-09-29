package com.kjs990114.goodong.adapter.out.persistence.mysql;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.PostEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.UserEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.mapper.PostMapper;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.PostRepository;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.UserRepository;
import com.kjs990114.goodong.application.port.out.db.DeletePostPort;
import com.kjs990114.goodong.application.port.out.db.LoadPostPort;
import com.kjs990114.goodong.application.port.out.db.SavePostPort;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostPersistenceAdapter implements SavePostPort, LoadPostPort, DeletePostPort {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Post save(Post post) {
        UserEntity userEntity = userRepository.findByUserId(post.getUser().getUserId()).orElseThrow(() -> new NotFoundException("User does not exists"));
        PostEntity postEntity = PostMapper.toEntity(post, userEntity);
        return PostMapper.toDomain(postRepository.save(postEntity));
    }

    @Override
    public Post loadByPostIdAndUserId(Long postId, Long viewerId) {
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post Not Founded"));
        return PostMapper.toDomain(postEntity);
    }

    @Override
    public Post loadByPostId(Long postId) {
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exists"));
        return PostMapper.toDomain(postEntity);
    }

    @Override
    public void delete(Long postId, Long userId) {
        PostEntity postEntity = postRepository.findByPostIdAndUserId(postId, userId).orElseThrow(() -> new NotFoundException("User Not found"));
        postEntity.softDelete();
        postRepository.save(postEntity);
    }
}
