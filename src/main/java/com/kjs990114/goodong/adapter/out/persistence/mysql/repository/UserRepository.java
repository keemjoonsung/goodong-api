package com.kjs990114.goodong.adapter.out.persistence.mysql.repository;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.ContributionEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.UserEntity;
import com.kjs990114.goodong.domain.user.Contribution;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT DISTINCT user FROM user user " +
            "LEFT JOIN FETCH user.contributions " +
            "WHERE user.email = :email AND user.deletedAt IS NULL")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT user FROM user user " +
            "LEFT JOIN FETCH user.contributions " +
            "WHERE user.nickname = :nickname AND user.deletedAt IS NULL")
    Optional<UserEntity> findByNickname(@Param("nickname") String nickname);

    @Query("SELECT DISTINCT user FROM user user " +
            "LEFT JOIN FETCH user.contributions " +
            "WHERE user.userId = :userId AND user.deletedAt IS NULL")
    Optional<UserEntity> findByUserId(@Param("userId") Long userId);


}
