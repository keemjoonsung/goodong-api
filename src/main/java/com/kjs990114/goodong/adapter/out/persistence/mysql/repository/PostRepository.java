package com.kjs990114.goodong.adapter.out.persistence.mysql.repository;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.PostEntity;
import com.kjs990114.goodong.application.dto.ModelInfoDTO;
import com.kjs990114.goodong.application.dto.PostInfoDTO;
import com.kjs990114.goodong.application.dto.PostSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query("""
    SELECT new com.kjs990114.goodong.application.dto.PostSummaryDTO(
        p.postId, p.title, u.userId, u.email, u.nickname,
        p.status, p.lastModifiedAt,
        GROUP_CONCAT(t.tag),
        COUNT(DISTINCT l)
    )
    FROM post p
    LEFT JOIN user u ON p.userId = u.userId
    LEFT JOIN p.tags t
    LEFT JOIN likes l ON l.postId = p.postId
    WHERE p.userId = :userId
    AND (p.status = 'PUBLIC' OR p.userId = :viewerId)
    AND p.deletedAt IS NULL
    GROUP BY p.postId, u.userId
    """)
    Page<PostSummaryDTO> postSummaryDTOsByUserIdBasedOnViewerId(@Param("userId") Long userId, @Param("viewerId") Long viewerId, Pageable pageable);

    @Query("""
    SELECT new com.kjs990114.goodong.application.dto.PostSummaryDTO(
        p.postId, p.title, u.userId, u.email, u.nickname,
        p.status, p.lastModifiedAt,
        GROUP_CONCAT(t.tag),
        COUNT(DISTINCT l)
    )
    FROM post p
    LEFT JOIN user u ON p.userId = u.userId
    LEFT JOIN p.tags t
    LEFT JOIN likes l ON l.postId = p.postId
    WHERE l.userId = :likerId
    AND (p.status = 'PUBLIC' OR p.userId = :viewerId)
    AND p.deletedAt IS NULL
    GROUP BY p.postId, u.userId
    """)
    Page<PostSummaryDTO> postSummaryDTOsByLikerIdBasedOnViewerId(@Param("likerId") Long likerId, @Param("viewerId") Long viewerId, Pageable pageable);

    @Query("""
    SELECT new com.kjs990114.goodong.application.dto.PostInfoDTO(
        p.postId, p.title, p.content, p.status,
        u.userId, u.email, u.nickname,u.profileImage,
        p.createdAt, p.lastModifiedAt,
        GROUP_CONCAT(t.tag),
        COUNT(DISTINCT l),
        CASE WHEN COUNT(l2) > 0 THEN TRUE ELSE FALSE END
    )
    FROM post p
    LEFT JOIN tag t ON t.post.postId = p.postId
    LEFT JOIN likes l ON l.postId = p.postId
    LEFT JOIN likes l2 ON l2.postId = p.postId AND l2.userId = :viewerId
    INNER JOIN user u ON u.userId = p.userId AND u.deletedAt IS NULL
    WHERE p.postId = :postId AND p.deletedAt IS NULL
    GROUP BY p.postId, p.title, p.content, p.status, u.userId, u.email, u.nickname,
             p.createdAt, p.lastModifiedAt
    """)
    Optional<PostInfoDTO> postInfoDTOsByPostIdAndViewerId(@Param("postId") Long postId, @Param("viewerId") Long viewerId);


    @Query("""
    SELECT new com.kjs990114.goodong.application.dto.ModelInfoDTO(
        m.version,m.fileName,m.commitMessage
    )
    FROM model m
    WHERE m.post.postId = :postId
    """)
    List<ModelInfoDTO> modelInfoDTOsByPostId(@Param("postId")Long postId);

    @Query("""
    SELECT DISTINCT m.post
    FROM model m
    LEFT JOIN FETCH m.post.models
    WHERE m.fileName = :fileName AND m.post.deletedAt IS NULL
    """)
    Optional<PostEntity> findPostIdByFileName(@Param("fileName") String fileName);

    @Query("""
    SELECT DISTINCT p
    FROM post p
    LEFT JOIN FETCH p.models
    WHERE p.postId = :postId AND p.deletedAt IS NULL
    """)
    Optional<PostEntity> findByPostId(@Param("postId") Long postId);

    @Query("""
    SELECT DISTINCT p
    FROM post p
    LEFT JOIN FETCH p.models
    WHERE p.postId = :postId AND p.userId = :userId AND p.deletedAt IS NULL
    """)
    Optional<PostEntity> findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query("""
    SELECT new com.kjs990114.goodong.application.dto.PostSummaryDTO(
        p.postId, p.title, u.userId, u.email, u.nickname,
        p.status, p.lastModifiedAt,
        GROUP_CONCAT(t.tag),
        COUNT(DISTINCT l)
    )
    FROM post p
    LEFT JOIN user u ON p.userId = u.userId
    LEFT JOIN p.tags t
    LEFT JOIN likes l ON l.postId = p.postId
    WHERE p.postId IN :postIds
    AND p.status = 'PUBLIC'
    AND p.deletedAt IS NULL
    AND u.deletedAt IS NULL
    GROUP BY p.postId, u.userId
    """)
    List<PostSummaryDTO> postSummaryDTOsByPostIds(@Param("postIds") List<Long> postIds);

    @Query("""
    SELECT COUNT(p) > 0
    FROM post p, model m
    WHERE m.post.postId = p.postId AND m.fileName = :fileName AND (p.userId = :userId OR p.status = 'PUBLIC') AND p.deletedAt IS NULL
    """)
    boolean isAccessibleByUserId(@Param("userId") Long userId, @Param("fileName") String fileName);

    @Query("""
    SELECT COUNT(p) > 0
    FROM post p
    WHERE p.title = :title AND p.userId = :userId AND p.deletedAt IS NULL
    """)
    boolean existsByTitleAndUserId(@Param("title") String title , @Param("userId") Long userId);

}
