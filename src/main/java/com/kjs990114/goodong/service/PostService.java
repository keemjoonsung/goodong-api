//package com.kjs990114.goodong.service;
//
//
//import com.kjs990114.goodong.infrastructure.post.elasticsearch.PostDocument;
//import com.kjs990114.goodong.presentation.dto.PostDTO;
//import com.kjs990114.goodong.entity.PostEntity;
//import com.kjs990114.goodong.repository.PostRepository;
//import com.kjs990114.goodong.domain.post.repository.PostSearchRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Service
//public class PostService {
//
//    private final PostRepository postRepository;
//    private final PostSearchRepository postSearchRepository;
//
//    @Transactional
//    public void savePost(PostDTO post) throws Exception {
//        System.out.println(post.getContent() + " ," + post.getFileUrl());
//        if(postRepository.existsByUserIdAndTitle(post.getUserId(), post.getTitle())){
//            throw new Exception("Title already Exists!");
//        }
//        PostEntity postEntity = new PostEntity();
//
//        postEntity.setTitle(post.getTitle());
//        postEntity.setContent(post.getContent());
//        postEntity.setUserId(post.getUserId());
//        postEntity.setUploadDate(post.getUploadDate());
//        postEntity.setFileUrl(post.getFileUrl());
//        PostDocument postDocument = new PostDocument();
//
//        postRepository.save(postEntity);
//        System.out.println("postEntity.getPostId() = " + postEntity.getPostId());
//        postDocument.setPostId(postEntity.getPostId());
//        postDocument.setContent(postEntity.getContent());
//        postDocument.setTitle(postEntity.getTitle());
//
//        postSearchRepository.save(postDocument);
//
//        System.out.println("postDocument.getId() = " + postDocument.getId());
//    }
//    @Transactional
//    public List<PostDocument> searchPosts(String keyword) {
//        List<PostDocument> posts = postSearchRepository.findByTitleContainingOrContentContaining(keyword, keyword);
//        return posts.stream()
//                .sorted((p1, p2) -> {
//                    boolean p1TitleContains = p1.getTitle().contains(keyword);
//                    boolean p2TitleContains = p2.getTitle().contains(keyword);
//                    if (p1TitleContains && !p2TitleContains) {
//                        return -1;
//                    } else if (!p1TitleContains && p2TitleContains) {
//                        return 1;
//                    } else {
//                        return 0;
//                    }
//                })
//                .limit(100)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public List<PostEntity> getPostByUserId(String username) {
//        return postRepository.findByUserId(username);
//    }
//
//    @Transactional
//    public PostEntity getPostByPostId(Long postId) { return postRepository.findByPostId(postId); }
//}
