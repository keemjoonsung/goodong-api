package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.CommentRepository;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.PostRepository;
import com.kjs990114.goodong.application.dto.CommentInfoDTO;
import com.kjs990114.goodong.application.dto.ModelInfoDTO;
import com.kjs990114.goodong.application.dto.PostInfoDTO;
import lombok.RequiredArgsConstructor;
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
}
