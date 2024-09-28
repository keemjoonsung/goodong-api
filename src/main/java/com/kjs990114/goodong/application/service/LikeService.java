package com.kjs990114.goodong.application.service;

import com.kjs990114.goodong.adapter.out.persistence.entity.UserEntity;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.adapter.out.persistence.entity.LikeEntity;
import com.kjs990114.goodong.adapter.out.persistence.entity.PostEntity;
import com.kjs990114.goodong.adapter.out.persistence.repository.PostRepository;
import com.kjs990114.goodong.adapter.out.persistence.repository.UserRepository;

import com.kjs990114.goodong.adapter.in.web.dto.DTOMapper;
import com.kjs990114.goodong.adapter.in.web.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String,Object> redisTemplate;

    @Transactional

    public void likePost(Long postId, Long likerId) {
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        UserEntity userEntity = userRepository.findByUserId(likerId).orElseThrow(() -> new NotFoundException("User does not exist"));

        if (postEntity.getStatus().equals(PostEntity.PostStatus.PRIVATE)) {
            if (!postEntity.getUser().getUserId().equals(userEntity.getUserId())) {
                throw new NotFoundException("User Authorization failed");
            }
        }

        LikeEntity likeEntity = LikeEntity.builder()
                .post(postEntity)
                .user(userEntity)
                .build();
        postEntity.like(likeEntity);
        userEntity.like(likeEntity);
        postRepository.save(postEntity);
        userRepository.save(userEntity);
        redisTemplate.delete("userPosts:" + postEntity.getUser().getUserId());
    }

    @Transactional
    public void unlikePost(Long postId, Long likerId) {
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        UserEntity userEntity = userRepository.findByUserId(likerId).orElseThrow(() -> new NotFoundException("User does not exist"));

        if (postEntity.getStatus().equals(PostEntity.PostStatus.PRIVATE)) {
            if (!postEntity.getUser().getUserId().equals(userEntity.getUserId())) {
                throw new UnAuthorizedException("User Authorization failed");
            }
        }

        LikeEntity likeEntity = postEntity.getLikes().stream().filter(l ->
                l.getUser().getUserId().equals(userEntity.getUserId())
        ).findFirst().orElseThrow(() -> new NotFoundException("Like does not exist"));

        postEntity.unLike(likeEntity);
        userEntity.unlike(likeEntity);
        postRepository.save(postEntity);
        userRepository.save(userEntity);
        redisTemplate.delete("userPosts:" + postEntity.getUser().getUserId());
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long postId, Long likerId) {
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        return postEntity.getLikes().stream().anyMatch(likeEntity ->
                likeEntity.getUser().getUserId().equals(likerId));
    }


    @Transactional(readOnly = true)
    public List<PostDTO.Summary> getLikedPosts(Long userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        return userEntity.getLikes().stream()
                .sorted(Comparator.comparing(LikeEntity::getCreatedAt))
                .map(LikeEntity::getPost).toList().stream()
                .map(DTOMapper::postToSummary)
                .toList();
    }
}
