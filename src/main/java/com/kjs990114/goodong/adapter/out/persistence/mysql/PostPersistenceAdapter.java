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
import com.kjs990114.goodong.common.exception.NotFoundException;
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
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post Not Founded"));
        return PostMapper.toDomain(postEntity);
    }

    @Override
    public Post loadByPostId(Long postId) {
        PostEntity postEntity = postRepository.findByPostId(postId).orElseThrow(() -> new NotFoundException("Post does not exists"));
        return PostMapper.toDomain(postEntity);
    }

    @Override
    public PostDetailDTO loadDetailByPostIdBasedOnViewerId(Long postId, Long viewerId) {
        PostInfoDTO postInfoDTO = postRepository.findPostInfoByPostIdAndViewerId(postId, viewerId).orElseThrow(()-> new NotFoundException("Post not Found"));
        List<ModelInfoDTO> modelInfoDTOs = postRepository.findModelInfosByPostId(postId);
        List<CommentInfoDTO> commentInfoDTOs = commentRepository.getCommentInfoDTOByPostId(postId);
        return PostDetailDTO.of(postInfoDTO,modelInfoDTOs,commentInfoDTOs);
    }

    @Override
    public List<Post> loadByPostIds(List<Long> postIds) {
        List<PostEntity> postEntities = postRepository.findAllByPostIds(postIds);
        return postEntities.stream().map(PostMapper::toDomain).toList();
    }

    @Override
    public Page<PostSummaryDTO> loadPageByUserIdBasedOnViewerId(Long userId, Long viewerId, Pageable pageable) {
        return postRepository.findUserPostsBasedOnViewer(userId,viewerId,pageable);
    }

    @Override
    public boolean existsByUserIdAndFileName(Long userId, String fileName) {
        return postRepository.existsByUserIdAndFileName(userId, fileName);
    }

    @Override
    public void delete(Long postId, Long userId) {
        PostEntity postEntity = postRepository.findByPostIdAndUserId(postId, userId).orElseThrow(() -> new NotFoundException("User Not found"));
        postEntity.softDelete();
        postRepository.save(postEntity);
    }
}
