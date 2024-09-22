package com.kjs990114.goodong.application.post;

import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.domain.post.Like;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.post.repository.PostRepository;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;

import com.kjs990114.goodong.presentation.dto.DTOMapper;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional

    public void likePost(Long postId, Long likerId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        User user = userRepository.findById(likerId).orElseThrow(() -> new NotFoundException("User does not exist"));

        if (post.getStatus().equals(Post.PostStatus.PRIVATE)) {
            if (!post.getUser().getUserId().equals(user.getUserId())) {
                throw new NotFoundException("User Authorization failed");
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

    public void unlikePost(Long postId, Long likerId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        User user = userRepository.findById(likerId).orElseThrow(() -> new NotFoundException("User does not exist"));

        if (post.getStatus().equals(Post.PostStatus.PRIVATE)) {
            if (!post.getUser().getUserId().equals(user.getUserId())) {
                throw new UnAuthorizedException("User Authorization failed");
            }
        }

        Like like = post.getLikes().stream().filter(l ->
                l.getUser().getUserId().equals(user.getUserId())
        ).findFirst().orElseThrow(() -> new NotFoundException("Like does not exist"));

        post.unLike(like);
        user.unlike(like);
        postRepository.save(post);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long postId, Long likerId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        return post.getLikes().stream().anyMatch(like ->
                like.getUser().getUserId().equals(likerId));
    }


    @Transactional(readOnly = true)
    public int getLikesCount(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        return post.getLikes().size();
    }

    @Transactional(readOnly = true)
    public List<PostDTO.Summary> getLikedPosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        return user.getLikes().stream()
                .sorted(Comparator.comparing(Like::getCreatedAt))
                .map(Like::getPost).toList().stream()
                .map(DTOMapper::postToSummary)
                .toList();
    }
}
