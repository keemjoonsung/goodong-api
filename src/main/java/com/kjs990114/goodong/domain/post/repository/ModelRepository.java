package com.kjs990114.goodong.domain.post.repository;

import com.kjs990114.goodong.domain.post.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model,Long> {
    Optional<Model> findByFileName(String fileName);
}
