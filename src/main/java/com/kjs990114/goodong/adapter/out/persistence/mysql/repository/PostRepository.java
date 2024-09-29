package com.kjs990114.goodong.adapter.out.persistence.mysql.repository;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.PostEntity;
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

    @Query("SELECT p FROM post p WHERE p.user.userId = :userId AND p.status = 'PUBLIC' AND p.deletedAt IS NULL")
    Page<PostEntity> findUserPublicPosts(@Param("userId") Long userId , Pageable pageable);

    @Query("SELECT p FROM post p WHERE p.user.userId = :userId AND p.deletedAt IS NULL")
    Page<PostEntity> findUserPublicAndPrivatePosts(@Param("userId")Long userId, Pageable pageable);

    @Query("SELECT m.post From model m WHERE m.fileName = :fileName AND m.post.deletedAt IS NULL")
    Optional<PostEntity> findPostIdByFileName(@Param("fileName")String fileName);

    @Query("SELECT p FROM post p WHERE p.postId = :postId AND p.deletedAt IS NULL")
    Optional<PostEntity> findByPostId(@Param("postId") Long postId);

    @Query("SELECT p FROM post p WHERE p.user.userId = :userId AND p.deletedAt IS NULL")
    List<PostEntity> findUserPostsAll(@Param("userId") Long userId);
}