package com.kjs990114.goodong.adapter.out.persistence.mysql.repository;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<LikeEntity,Long> {

    @Modifying
    @Query("""
    DELETE FROM likes l
    WHERE l.postId = :postId
    AND l.userId = :userId
    """)
    void deleteByPostIdAndUserId(@Param("postId") Long postId,@Param("userId") Long userId);

}
