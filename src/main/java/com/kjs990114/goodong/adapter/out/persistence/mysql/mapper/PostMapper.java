package com.kjs990114.goodong.adapter.out.persistence.mysql.mapper;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.*;
import com.kjs990114.goodong.domain.post.Post;

public class PostMapper {

//    public static Post toDomain(PostEntity postEntity) {
//
//    }

    public static PostEntity toEntity(Post post) {
        return PostEntity.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .user(UserEntity.of(post.getUser().getUserId()))
                .status(post.getStatus())
                .models(post.getModels().stream().map(
                        model -> ModelEntity.builder()
                                .modelId(model.getModelId())
                                .post(PostEntity.of(model.getPost().getPostId()))
                                .commitMessage(model.getCommitMessage())
                                .fileName(model.getFileName())
                                .build()).toList())
                .commentEntities(post.getComments().stream().map(
                        comment -> CommentEntity.builder()
                                .commentId(comment.getCommentId())
                                .post(PostEntity.of(comment.getPost().getPostId()))
                                .user(UserEntity.of(comment.getUser().getUserId()))
                                .content(comment.getContent())
                                .build()).toList())
                .likes(post.getLikes().stream().map(
                        like -> LikeEntity.builder()
                                .likeId(like.getLikeId())
                                .post(PostEntity.of(like.getPost().getPostId()))
                                .user(UserEntity.of(like.getUser().getUserId()))
                                .build()).toList())
                .tags(post.getTags().stream().map(
                        tag -> TagEntity.builder()
                                .tagId(tag.getTagId())
                                .post(PostEntity.of(tag.getPost().getPostId()))
                                .tag(tag.getTag())
                                .build()).toList())
                .build();
    }

    private static ModelEntity toModelEntity(Post post) {
        return new ModelEntity();
    }
}
