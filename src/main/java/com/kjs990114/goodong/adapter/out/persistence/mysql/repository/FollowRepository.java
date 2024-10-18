package com.kjs990114.goodong.adapter.out.persistence.mysql.repository;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.FollowEntity;
import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    @Modifying
    @Query("""
    DELETE FROM follow f
    WHERE f.followerId = :followerId AND f.followeeId = :followeeId
    """)
    void deleteByFollowerIdAndFolloweeId(@Param("followerId")Long followerId, @Param("followeeId")Long followeeId);

    @Query("""
    SELECT new com.kjs990114.goodong.application.dto.UserSummaryDTO(
        u.userId, u.email, u.nickname, u.profileImage
    )
    FROM follow f
    LEFT JOIN user u ON f.followeeId = u.userId
    WHERE f.followerId = :userId
    AND u.deletedAt IS NULL
    """)
    Page<UserSummaryDTO> findFollowingsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT new com.kjs990114.goodong.application.dto.UserSummaryDTO(
        u.userId, u.email, u.nickname, u.profileImage
    )
    FROM follow f
    LEFT JOIN user u ON f.followerId = u.userId
    WHERE f.followeeId = :userId
    AND u.deletedAt IS NULL
    """)
    Page<UserSummaryDTO> findFollowersByUserId(@Param("userId") Long userId, Pageable pageable);
}
