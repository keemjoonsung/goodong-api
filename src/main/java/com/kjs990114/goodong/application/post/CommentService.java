package com.kjs990114.goodong.application.post;

import com.kjs990114.goodong.application.user.UserService;
import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.domain.post.Comment;
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
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void addComment(Long postId, String email, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GlobalException("Post does not exist"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GlobalException("User does not exist"));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);
        post.addComment(comment);
        user.addComment(comment);

        postRepository.save(post);
        userRepository.save(user);
    }

    public void deleteComment(Long commentId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GlobalException("User does not exist"));
        Comment comment = user.getComments().stream()
                .filter(c -> c.getCommentId().equals(commentId)).findFirst().orElseThrow(() -> new GlobalException("Comment does not exist"));
        Post post = comment.getPost();

        if (!comment.getUser().getEmail().equals(email)) {
            throw new GlobalException("Authorization Failed");
        }
        user.deleteComment(comment);
        post.deleteComment(comment);

        userRepository.save(user);
        postRepository.save(post);
    }

    public void updateComment(Long commentId, String email, String content) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GlobalException("User does not exist"));
        Comment comment = user.getComments().stream()
                .filter(c -> c.getCommentId().equals(commentId)).findFirst().orElseThrow(() -> new GlobalException("Comment does not exist"));
        Post post = comment.getPost();

        if (!comment.getUser().getEmail().equals(email)) {
            throw new GlobalException("Authorization Failed");
        }
        comment.setContent(content);

        userRepository.save(user);
        postRepository.save(post);
    }
}
