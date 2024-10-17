package com.kjs990114.goodong.adapter.out.persistence.mysql;

import com.kjs990114.goodong.adapter.out.persistence.mysql.mapper.FollowMapper;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.FollowRepository;
import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import com.kjs990114.goodong.application.port.out.db.DeleteFollowPort;
import com.kjs990114.goodong.application.port.out.db.LoadFollowPort;
import com.kjs990114.goodong.application.port.out.db.SaveFollowPort;
import com.kjs990114.goodong.domain.follow.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowPersistenceAdapter implements SaveFollowPort, DeleteFollowPort, LoadFollowPort {

    private final FollowRepository followRepository;

    @Override
    public void delete(Long followerId, Long followeeId) {
        followRepository.deleteByFollowerIdAndFolloweeId(followerId,followeeId);
    }

    @Override
    public void save(Follow follow) {
        followRepository.save(FollowMapper.toFollowEntity(follow));
    }

    @Override
    public Page<UserSummaryDTO> loadFollowings(Long userId, Pageable pageable) {
        return followRepository.findFollowingsByUserId(userId, pageable);
    }

    @Override
    public Page<UserSummaryDTO> loadFollowers(Long userId, Pageable pageable) {
        return followRepository.findFollowersByUserId(userId, pageable);
    }
}
