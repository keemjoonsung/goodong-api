package com.kjs990114.goodong.adapter.out.persistence.mysql.repository;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
}
