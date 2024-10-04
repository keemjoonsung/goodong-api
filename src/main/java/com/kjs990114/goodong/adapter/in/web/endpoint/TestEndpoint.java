package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.CommentRepository;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.FollowRepository;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.PostRepository;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.UserRepository;
import com.kjs990114.goodong.application.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestEndpoint {
    private final CommentRepository postDetailQueryDao;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @GetMapping("/1")
    public List<CommentInfoDTO> t1(){
        return postDetailQueryDao.getCommentInfoDTOByPostId(1L);
    }
    @GetMapping("/2")
    public PostInfoDTO t2(){
        return postRepository.findPostInfoByPostIdAndViewerId(2L,1L).orElse(null);
    }
    @GetMapping("/3")
    public List<ModelInfoDTO> t3(@RequestParam(value = "postId", defaultValue = "1") Long postId){
        return postRepository.findModelInfosByPostId(postId);
    }
    @GetMapping("/4")
    public UserDetailDTO t4(){
        return userRepository.findUserInfoByUserIdAndViewerId(1L,1L);
    }
    @GetMapping("/5")
    public Page<PostSummaryDTO> t5(){
        return postRepository.loadPageByLikerIdBasedOnViewerId(1L,1L, Pageable.unpaged());
    }
    @GetMapping("/6")
    public Page<UserSummaryDTO> t6(){
        return followRepository.findFollowingsByUserId(1L, Pageable.unpaged());
    }
    @GetMapping("/7")
    public Page<UserSummaryDTO> t7(){
        return followRepository.findFollowersByUserId(1L, Pageable.unpaged());
    }
}
