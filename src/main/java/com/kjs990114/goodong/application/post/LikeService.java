package com.kjs990114.goodong.application.post;

import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.domain.post.Like;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.post.repository.PostRepository;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void likePost(Long postId, String email) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GlobalException("Post does not exist"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GlobalException("User does not exist"));

        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();
        post.like(like);
        user.like(like);
        postRepository.save(post);
        userRepository.save(user);
    }

    public void unlikePost(Long postId, String email) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GlobalException("Post does not exist"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GlobalException("User does not exist"));

        Like like = post.getLikes().stream().filter(l ->
                l.getUser().getUserId().equals(user.getUserId())
        ).findFirst().orElseThrow(() -> new GlobalException("Like does not exist"));

        post.unLike(like);
        user.unlike(like);
        postRepository.save(post);
        userRepository.save(user);
    }
}
