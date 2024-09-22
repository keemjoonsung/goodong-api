package com.kjs990114.goodong.domain.post.repository;

import com.kjs990114.goodong.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM post p WHERE p.user.userId = :userId AND (p.status = 'PUBLIC' OR p.user.userId = :viewerId)")
    Page<Post> findUserPosts(@Param("userId") Long userId , @Param("viewerId") Long viewerId , Pageable pageable);

    @Query("SELECT m.post From Model m WHERE m.fileName = :fileName")
    Optional<Post> findPostIdByFileName(@Param("fileName")String fileName);

}
