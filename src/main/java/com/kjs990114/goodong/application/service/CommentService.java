package com.kjs990114.goodong.application.service;

import com.kjs990114.goodong.adapter.out.persistence.entity.Comment;
import com.kjs990114.goodong.adapter.out.persistence.entity.UserEntity;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.adapter.out.persistence.entity.PostEntity;
import com.kjs990114.goodong.adapter.out.persistence.repository.PostRepository;
import com.kjs990114.goodong.adapter.out.persistence.repository.UserRepository;
import com.kjs990114.goodong.adapter.in.web.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addComment(Long postId, String email, String content) {
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User does not exist"));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(userEntity);
        comment.setPost(postEntity);
        postEntity.addComment(comment);
        userEntity.addComment(comment);

        postRepository.save(postEntity);
        userRepository.save(userEntity);
    }
    @Transactional
    public void deleteComment(Long postId ,Long commentId, String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User does not exist"));
        Comment comment = userEntity.getComments().stream()
                .filter(c -> c.getCommentId().equals(commentId)).findFirst().orElseThrow(() -> new NotFoundException("Comment does not exist"));
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));

        if (!comment.getUser().getEmail().equals(email)) {
            throw new UnAuthorizedException("Authorization Failed");
        }
        userEntity.deleteComment(comment);
        postEntity.deleteComment(comment);

        userRepository.save(userEntity);
        postRepository.save(postEntity);
    }

    @Transactional
    public void updateComment(Long postId, Long commentId, String email, String content) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User does not exist"));
        Comment comment = userEntity.getComments().stream()
                .filter(c -> c.getCommentId().equals(commentId)).findFirst().orElseThrow(() -> new NotFoundException("Comment does not exist"));
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        if (!comment.getUser().getEmail().equals(email)) {
            throw new UnAuthorizedException("Authorization Failed");
        }
        comment.setContent(content);

        userRepository.save(userEntity);
        postRepository.save(postEntity);
    }

    @Transactional(readOnly = true)
    public List<PostDTO.CommentInfo> getComments(Long postId){
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
        return postEntity.getComments().stream().map(
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
