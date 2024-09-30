package com.kjs990114.goodong.adapter.out.persistence.mysql.mapper;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.ContributionEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.FollowEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.UserEntity;
import com.kjs990114.goodong.domain.user.Contribution;
import com.kjs990114.goodong.domain.user.Follow;
import com.kjs990114.goodong.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
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
                .followers(user.getFollowers()
                        .stream()
                        .map(UserMapper::toFollowEntity).collect(Collectors.toSet()))
                .followings(user.getFollowings()
                        .stream()
                        .map(UserMapper::toFollowEntity).collect(Collectors.toSet()))
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
                .followers(userEntity.getFollowers().stream().map(UserMapper::toFollowDomain).collect(Collectors.toSet()))
                .followings(userEntity.getFollowings().stream().map(UserMapper::toFollowDomain).collect(Collectors.toSet()))
                .build();

    }

    private static Follow toFollowDomain(FollowEntity followEntity) {
        return Follow.builder()
                .id(followEntity.getId())
                .follower(User.builder().userId(followEntity.getFollower().getUserId()).build())
                .followee(User.builder().userId(followEntity.getFollowee().getUserId()).build())
                .build();
    }

    private static FollowEntity toFollowEntity(Follow follow) {
        return FollowEntity.builder()
                .id(follow.getId())
                .follower(UserEntity.builder().userId(follow.getFollower().getUserId()).build())
                .followee(UserEntity.builder().userId(follow.getFollowee().getUserId()).build())
                .build();
    }
}
