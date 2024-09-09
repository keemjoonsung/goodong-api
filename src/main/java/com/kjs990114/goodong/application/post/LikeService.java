package com.kjs990114.goodong.application.post;

import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.domain.post.Like;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.post.repository.PostRepository;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(value = "isLike")
    public void likePost(Long postId, Long likerId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GlobalException("Post does not exist"));
        User user = userRepository.findById(likerId).orElseThrow(() -> new GlobalException("User does not exist"));

        if(post.getStatus().equals(Post.PostStatus.PRIVATE)){
            if(!post.getUser().getUserId().equals(user.getUserId())){
                throw new GlobalException("User Authorization failed");
            }
        }

        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();
        post.like(like);
        user.like(like);
        postRepository.save(post);
        userRepository.save(user);
    }
    @Transactional
    @CacheEvict(value = "isLike")
    public void unlikePost(Long postId, Long likerId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GlobalException("Post does not exist"));
        User user = userRepository.findById(likerId).orElseThrow(() -> new GlobalException("User does not exist"));

        if(post.getStatus().equals(Post.PostStatus.PRIVATE)){
            if(!post.getUser().getUserId().equals(user.getUserId())){
                throw new GlobalException("User Authorization failed");
            }
        }

        Like like = post.getLikes().stream().filter(l ->
                l.getUser().getUserId().equals(user.getUserId())
        ).findFirst().orElseThrow(() -> new GlobalException("Like does not exist"));

        post.unLike(like);
        user.unlike(like);
        postRepository.save(post);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "isLiked")
    public boolean isLiked(Long postId, Long likerId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new GlobalException("Post does not exist"));
        return post.getLikes().stream().anyMatch(like ->
                like.getUser().getUserId().equals(likerId));
    }
}
