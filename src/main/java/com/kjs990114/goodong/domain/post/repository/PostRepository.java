package com.kjs990114.goodong.domain.post.repository;

import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUser(User user);
}
