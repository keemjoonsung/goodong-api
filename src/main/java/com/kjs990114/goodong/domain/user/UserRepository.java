package com.kjs990114.goodong.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.email = :email AND user.deletedAt IS NULL")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT user FROM User user WHERE user.nickname = :nickname AND user.deletedAt IS NULL")
    Optional<User> findByNickname(@Param("nickname") String nickname);

    @Query("SELECT user FROM User user WHERE user.userId = :userId AND user.deletedAt IS NULL")
    Optional<User> findByUserId(@Param("userId") Long userId);

}
