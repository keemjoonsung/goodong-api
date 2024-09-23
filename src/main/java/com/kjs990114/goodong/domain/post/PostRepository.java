package com.kjs990114.goodong.domain.post;

import com.kjs990114.goodong.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM post p WHERE p.user.userId = :userId AND p.deletedAt IS NULL AND (p.status = 'PUBLIC' OR p.user.userId = :viewerId)")
    Page<Post> findUserPosts(@Param("userId") Long userId , @Param("viewerId") Long viewerId , Pageable pageable);

    @Query("SELECT m.post From Model m WHERE m.fileName = :fileName AND m.post.deletedAt IS NULL")
    Optional<Post> findPostIdByFileName(@Param("fileName")String fileName);

    @Query("SELECT p FROM post p WHERE p.postId = :postId AND p.deletedAt IS NULL")
    Optional<Post> findByPostId(@Param("postId") Long postId);

    @Query("SELECT p FROM post p WHERE p.user.userId = :userId AND p.deletedAt IS NULL")
    List<Post> findUserPostsAll(@Param("userId") Long userId);
}
