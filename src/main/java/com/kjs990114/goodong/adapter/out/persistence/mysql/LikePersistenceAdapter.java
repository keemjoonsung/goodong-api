package com.kjs990114.goodong.adapter.out.persistence.mysql;

import com.kjs990114.goodong.adapter.out.persistence.mysql.mapper.LikeMapper;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.LikeRepository;
import com.kjs990114.goodong.application.port.out.db.DeleteLikePort;
import com.kjs990114.goodong.application.port.out.db.SaveLikePort;
import com.kjs990114.goodong.domain.like.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class LikePersistenceAdapter implements SaveLikePort , DeleteLikePort {

    private final LikeRepository likeRepository;

    @Override
    public Long save(Like like) {
        return likeRepository.save(LikeMapper.toEntity(like)).getUserId();
    }

    @Transactional
    @Override
    public void delete(Long postId, Long userId) {
        likeRepository.deleteByPostIdAndUserId(postId,userId);
    }
}
