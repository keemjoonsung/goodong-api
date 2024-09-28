package com.kjs990114.goodong.adapter.out.persistence.repository;

import com.kjs990114.goodong.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT user FROM user user WHERE user.email = :email AND user.deletedAt IS NULL")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query("SELECT user FROM user user WHERE user.nickname = :nickname AND user.deletedAt IS NULL")
    Optional<UserEntity> findByNickname(@Param("nickname") String nickname);

    @Query("SELECT user FROM user user WHERE user.userId = :userId AND user.deletedAt IS NULL")
    Optional<UserEntity> findByUserId(@Param("userId") Long userId);

}
