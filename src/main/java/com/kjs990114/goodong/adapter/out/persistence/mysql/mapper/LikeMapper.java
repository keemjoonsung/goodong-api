package com.kjs990114.goodong.adapter.out.persistence.mysql.mapper;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.LikeEntity;
import com.kjs990114.goodong.domain.like.Like;

public class LikeMapper {

    public static Like toDomain(LikeEntity likeEntity){
        return Like.builder()
                .likeId(likeEntity.getLikeId())
                .postId(likeEntity.getPostId())
                .userId(likeEntity.getUserId())
                .build();
    }

    public static LikeEntity toEntity(Like like){
        return LikeEntity.builder()
                .likeId(like.getLikeId())
                .postId(like.getPostId())
                .userId(like.getUserId())
                .build();
    }
}
