//package com.kjs990114.goodong.application.service;
//
//import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.CommentEntity;
//import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.UserEntity;
//import com.kjs990114.goodong.common.exception.NotFoundException;
//import com.kjs990114.goodong.common.exception.UnAuthorizedException;
//import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.PostEntity;
//import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.PostRepository;
//import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.UserRepository;
//import com.kjs990114.goodong.adapter.in.web.dto.PostDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CommentService {
//    private final PostRepository postRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public void addComment(Long postId, String email, String content) {
//        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
//        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User does not exist"));
//        CommentEntity commentEntity = new CommentEntity();
//        commentEntity.setContent(content);
//        commentEntity.setUser(userEntity);
//        commentEntity.setPost(postEntity);
//        postEntity.addComment(commentEntity);
//        userEntity.addComment(commentEntity);
//
//        postRepository.save(postEntity);
//        userRepository.save(userEntity);
//    }
//    @Transactional
//    public void deleteComment(Long postId ,Long commentId, String email) {
//        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User does not exist"));
//        CommentEntity commentEntity = userEntity.getComments().stream()
//                .filter(c -> c.getCommentId().equals(commentId)).findFirst().orElseThrow(() -> new NotFoundException("Comment does not exist"));
//        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
//
//        if (!commentEntity.getUser().getEmail().equals(email)) {
//            throw new UnAuthorizedException("Authorization Failed");
//        }
//        userEntity.deleteComment(commentEntity);
//        postEntity.deleteComment(commentEntity);
//
//        userRepository.save(userEntity);
//        postRepository.save(postEntity);
//    }
//
//    @Transactional
//    public void updateComment(Long postId, Long commentId, String email, String content) {
//        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User does not exist"));
//        CommentEntity commentEntity = userEntity.getComments().stream()
//                .filter(c -> c.getCommentId().equals(commentId)).findFirst().orElseThrow(() -> new NotFoundException("Comment does not exist"));
//        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
//        if (!commentEntity.getUser().getEmail().equals(email)) {
//            throw new UnAuthorizedException("Authorization Failed");
//        }
//        commentEntity.setContent(content);
//
//        userRepository.save(userEntity);
//        postRepository.save(postEntity);
//    }
//
//    @Transactional(readOnly = true)
//    public List<PostDTO.CommentInfo> getComments(Long postId){
//        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exist"));
//        return postEntity.getCommentEntities().stream().map(
//                commentEntity -> PostDTO.CommentInfo.builder()
//                        .commentId(commentEntity.getCommentId())
//                        .userId(commentEntity.getUser().getUserId())
//                        .email(commentEntity.getUser().getEmail())
//                        .nickname(commentEntity.getUser().getNickname())
//                        .content(commentEntity.getContent())
//                        .createdAt(commentEntity.getCreatedAt())
//                        .lastModifiedAt(commentEntity.getLastModifiedAt())
//                        .build()
//        ).toList();
//    }
//}
