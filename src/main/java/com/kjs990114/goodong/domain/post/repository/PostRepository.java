package com.kjs990114.goodong.domain.post.repository;

import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUser(User user);
    @Query("SELECT m.post From Model m WHERE m.fileName = :fileName")
    Optional<Post> findPostIdByFileName(@Param("fileName")String fileName);
}
