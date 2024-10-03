package com.kjs990114.goodong.adapter.out.persistence.mysql.mapper;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.ContributionEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.UserEntity;
import com.kjs990114.goodong.domain.user.Contribution;
import com.kjs990114.goodong.domain.user.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserEntity toEntity(User user) {

        return UserEntity.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .password(user.getPassword())
                .profileImage(user.getProfileImage())
                .role(user.getRole())
                .contributions(user.getContributions()
                        .stream()
                        .map(cont -> ContributionEntity.builder()
                                .contId(cont.getContId())
                                .count(cont.getCount())
                                .date(cont.getDate())
                                .user(UserEntity.of(user.getUserId()))
                                .build()
                        ).toList())

                .build();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .profileImage(userEntity.getProfileImage())
                .contributions(userEntity.getContributions().stream()
                        .map(contributionEntity -> Contribution.builder()
                                .contId(contributionEntity.getContId())
                                .count(contributionEntity.getCount())
                                .date(contributionEntity.getDate())
                                .user(User.of(contributionEntity.getUser().getUserId()))
                                .build()).collect(Collectors.toList()))
                .build();

    }
}
