package com.kjs990114.goodong.adapter.in.web.dto;

import com.kjs990114.goodong.adapter.out.persistence.entity.ModelEntity;
import com.kjs990114.goodong.adapter.out.persistence.entity.PostEntity;
import com.kjs990114.goodong.adapter.out.persistence.entity.TagEntity;
import com.kjs990114.goodong.adapter.out.persistence.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {

    public static PostDTO.Summary postToSummary(PostEntity postEntity) {
        UserEntity userEntity = postEntity.getUser();
        List<TagEntity> tagEntities = postEntity.getTags();
        return PostDTO.Summary.builder()
                .postId(postEntity.getPostId())
                .title(postEntity.getTitle())
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .status(postEntity.getStatus())
                .lastModifiedAt(postEntity.getLastModifiedAt())
                .tags(tagEntities.stream().map(TagEntity::getTag).collect(Collectors.toList()))
                .likes(postEntity.getLikeCount())
                .build();
    }

    public static PostDTO.PostDetail postToDetail(PostEntity postEntity) {
        List<ModelEntity> modelEntities = postEntity.getModels();
        UserEntity userEntity = postEntity.getUser();
        List<PostDTO.ModelInfo> model = modelEntities.stream().map(m ->
                PostDTO.ModelInfo.builder()
                        .version(m.getVersion())
                        .fileName(m.getFileName())
                        .commitMessage(m.getCommitMessage())
                        .build()
        ).toList();
        return PostDTO.PostDetail.builder()
                .postId(postEntity.getPostId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .status(postEntity.getStatus())
                .models(model)
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .createdAt(postEntity.getCreatedAt())
                .lastModifiedAt(postEntity.getLastModifiedAt())
                .tags(postEntity.getTags().stream().map(TagEntity::getTag).collect(Collectors.toList()))
                .likes(postEntity.getLikeCount())
                .build();
    }

    public static UserDTO.UserDetail userToDetail(UserEntity userEntity) {
        return UserDTO.UserDetail.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .profileImage(userEntity.getProfileImage())
                .build();
    }

    public static UserDTO.UserSummary userToSummary(UserEntity userEntity){
        return UserDTO.UserSummary.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .profileImage(userEntity.getProfileImage())
                .build();
    }
}
