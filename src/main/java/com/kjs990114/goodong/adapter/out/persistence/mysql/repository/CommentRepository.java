package com.kjs990114.goodong.adapter.out.persistence.mysql.repository;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.CommentEntity;
import com.kjs990114.goodong.application.dto.CommentInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

    @Query("""
            SELECT new com.kjs990114.goodong.application.dto.CommentInfoDTO(c.commentId, u.userId, u.email, u.nickname,u.profileImage,c.content, c.createdAt, c.lastModifiedAt)
            FROM comment c
            JOIN user u ON u.userId = c.userId
            WHERE c.postId = :postId
            AND c.deletedAt IS NULL
            """)
    List<CommentInfoDTO> getCommentInfoDTOByPostId(@Param("postId") Long postId);
    @Query("""
    SELECT c
    FROM comment c
    WHERE c.commentId = :commentId AND c.deletedAt IS NULL
    """)
    Optional<CommentEntity> findByCommentId(@Param("commentId") Long commentId);

    @Query("""
    SELECT c
    FROM comment c
    WHERE c.commentId = :commentId AND c.userId = :userId AND c.deletedAt IS NULL
    """)
    Optional<CommentEntity> findByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);
}
