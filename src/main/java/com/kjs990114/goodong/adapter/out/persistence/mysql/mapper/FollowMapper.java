package com.kjs990114.goodong.adapter.out.persistence.mysql.mapper;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.FollowEntity;
import com.kjs990114.goodong.domain.social.Follow;

public class FollowMapper {

    public static Follow toFollowDomain(FollowEntity followEntity) {
        return Follow.builder()
                .followId(followEntity.getId())
                .followerId(followEntity.getFollowerId())
                .followeeId(followEntity.getFolloweeId())
                .build();
    }

    public static FollowEntity toFollowEntity(Follow follow) {
        return FollowEntity.builder()
                .id(follow.getFollowId())
                .followerId(follow.getFollowerId())
                .followeeId(follow.getFolloweeId())
                .build();
    }
}
