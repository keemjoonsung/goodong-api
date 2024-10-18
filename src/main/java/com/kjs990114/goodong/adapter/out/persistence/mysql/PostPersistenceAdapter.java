package com.kjs990114.goodong.adapter.out.persistence.mysql;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.PostEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.mapper.PostMapper;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.CommentRepository;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.PostRepository;
import com.kjs990114.goodong.application.dto.*;
import com.kjs990114.goodong.application.dto.PostDTO.PostDetailDTO;
import com.kjs990114.goodong.application.port.out.db.DeletePostPort;
import com.kjs990114.goodong.application.port.out.db.LoadPostPort;
import com.kjs990114.goodong.application.port.out.db.SavePostPort;
import com.kjs990114.goodong.common.exception.Error;
import com.kjs990114.goodong.common.exception.ErrorException;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostPersistenceAdapter implements SavePostPort, LoadPostPort, DeletePostPort{

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public Post save(Post post) {
        PostEntity postEntity = PostMapper.toEntity(post);
        return PostMapper.toDomain(postRepository.save(postEntity));
    }

    @Override
    public Post loadByPostIdAndUserId(Long postId, Long viewerId) {
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new ErrorException(Error.POST_NOT_FOUND));
        return PostMapper.toDomain(postEntity);
    }

    @Override
    public boolean existsByTitleAndUserId(String title, Long userId) {
        return postRepository.existsByTitleAndUserId(title,userId);
    }


    @Override
    public PostDetailDTO postDetailDTOByPostIdBasedOnViewerId(Long postId, Long viewerId) {
        PostInfoDTO postInfoDTO = postRepository.postInfoDTOsByPostIdAndViewerId(postId, viewerId).orElseThrow(()-> new ErrorException(Error.POST_NOT_FOUND));
        List<ModelInfoDTO> modelInfoDTOs = postRepository.modelInfoDTOsByPostId(postId);
        List<CommentInfoDTO> commentInfoDTOs = commentRepository.getCommentInfoDTOByPostId(postId);
        return PostDetailDTO.of(postInfoDTO,modelInfoDTOs,commentInfoDTOs);
    }

    @Override
    public List<PostSummaryDTO> postSummaryDTOListByPostIds(List<Long> postIds) {
        return postRepository.postSummaryDTOsByPostIds(postIds);
    }

    @Override
    public Page<PostSummaryDTO> postSummaryDTOPageByUserIdBasedOnViewerId(Long userId, Long viewerId, Pageable pageable) {
        return postRepository.postSummaryDTOsByUserIdBasedOnViewerId(userId,viewerId,pageable);
    }

    @Override
    public Page<PostSummaryDTO> postSummaryDTOPageByLikerIdBasedOnViewerId(Long likerId, Long viewerId, Pageable pageable) {
        return postRepository.postSummaryDTOsByLikerIdBasedOnViewerId(likerId,viewerId,pageable);
    }

    @Override
    public boolean isAccessibleByUserId(Long userId, String fileName) {
        return postRepository.isAccessibleByUserId(userId, fileName);
    }

    @Override
    public void delete(Long postId, Long userId) {
        PostEntity postEntity = postRepository.findByPostIdAndUserId(postId, userId).orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND));
        postEntity.softDelete();
        postRepository.save(postEntity);
    }
}
