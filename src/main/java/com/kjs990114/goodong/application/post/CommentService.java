package com.kjs990114.goodong.application.post;

import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.domain.post.Comment;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.post.repository.PostRepository;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(value = "commentList", key= "#postId")
    public void addComment(Long postId, String email, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User does not exist"));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);
        post.addComment(comment);
        user.addComment(comment);

        postRepository.save(post);
        userRepository.save(user);
    }
    @Transactional
    @CacheEvict(value = "commentList", key= "#postId")
    public void deleteComment(Long postId ,Long commentId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User does not exist"));
        Comment comment = user.getComments().stream()
                .filter(c -> c.getCommentId().equals(commentId)).findFirst().orElseThrow(() -> new NotFoundException("Comment does not exist"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));

        if (!comment.getUser().getEmail().equals(email)) {
            throw new UnAuthorizedException("Authorization Failed");
        }
        user.deleteComment(comment);
        post.deleteComment(comment);

        userRepository.save(user);
        postRepository.save(post);
    }

    @Transactional
    @CacheEvict(value = "commentList", key= "#postId")
    public void updateComment(Long postId, Long commentId, String email, String content) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User does not exist"));
        Comment comment = user.getComments().stream()
                .filter(c -> c.getCommentId().equals(commentId)).findFirst().orElseThrow(() -> new NotFoundException("Comment does not exist"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        if (!comment.getUser().getEmail().equals(email)) {
            throw new UnAuthorizedException("Authorization Failed");
        }
        comment.setContent(content);

        userRepository.save(user);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "commentList",key = "#postId")
    public List<PostDTO.CommentInfo> getComments(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        return post.getComments().stream().map(
                comment -> PostDTO.CommentInfo.builder()
                        .commentId(comment.getCommentId())
                        .userId(comment.getUser().getUserId())
                        .email(comment.getUser().getEmail())
                        .nickname(comment.getUser().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .lastModifiedAt(comment.getLastModifiedAt())
                        .build()
        ).toList();
    }
}
