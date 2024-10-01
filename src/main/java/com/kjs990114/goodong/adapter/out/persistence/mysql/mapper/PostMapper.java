package com.kjs990114.goodong.adapter.out.persistence.mysql.mapper;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.*;
import com.kjs990114.goodong.domain.post.*;

import java.util.stream.Collectors;

public class PostMapper {

    public static Post toDomain(PostEntity postEntity) {
        return Post.builder()
                .postId(postEntity.getPostId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .user(UserMapper.toDomain(postEntity.getUser()))
                .status(postEntity.getStatus())
                .createdAt(postEntity.getCreatedAt())
                .lastModifiedAt(postEntity.getLastModifiedAt())
                .models(postEntity.getModels().stream().map(
                        modelEntity -> Model.builder()
                                .modelId(modelEntity.getModelId())
                                .version(modelEntity.getVersion())
                                .fileName(modelEntity.getFileName())
                                .commitMessage(modelEntity.getCommitMessage())
                                .post(Post.of(modelEntity.getPost().getPostId()))
                                .build()).collect(Collectors.toList()))
                .comments(postEntity.getComments().stream().map(
                        commentEntity -> Comment.builder()
                                .commentId(commentEntity.getCommentId())
                                .content(commentEntity.getContent())
                                .createdAt(commentEntity.getCreatedAt())
                                .lastModifiedAt(commentEntity.getLastModifiedAt())
                                .post(Post.of(commentEntity.getPost().getPostId()))
                                .user(UserMapper.toDomain(commentEntity.getUser()))
                                .build()).collect(Collectors.toList()))
                .likes(postEntity.getLikes().stream().map(
                        likeEntity -> Like.builder()
                                .likeId(likeEntity.getLikeId())
                                .user(UserMapper.toDomain(likeEntity.getUser()))
                                .post(Post.of(likeEntity.getPost().getPostId()))
                                .build()).collect(Collectors.toList()))
                .tags(postEntity.getTags().stream().map(
                        tagEntity -> Tag.builder()
                                .tagId(tagEntity.getTagId())
                                .post(Post.of(tagEntity.getPost().getPostId()))
                                .tag(tagEntity.getTag())
                                .build()).collect(Collectors.toList()))
                .build();
    }

    public static PostEntity toEntity(Post post, UserEntity userEntity) {
        PostEntity postEntity = PostEntity.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .user(userEntity)
                .status(post.getStatus())
                .build();

        postEntity.setModels(post.getModels().stream().map(
                model -> ModelEntity.builder()
                        .modelId(model.getModelId())
                        .version(model.getVersion())
                        .post(postEntity)
                        .commitMessage(model.getCommitMessage())
                        .fileName(model.getFileName())
                        .build()).toList());

        postEntity.setComments(post.getComments().stream().map(
                comment -> CommentEntity.builder()
                        .commentId(comment.getCommentId())
                        .post(postEntity)
                        .user(userEntity)
                        .content(comment.getContent())
                        .build()).toList());

        postEntity.setLikes(post.getLikes().stream().map(
                like -> LikeEntity.builder()
                        .likeId(like.getLikeId())
                        .post(postEntity)
                        .user(userEntity)
                        .build()).toList());

        postEntity.setTags(post.getTags().stream().map(
                tag -> TagEntity.builder()
                        .tagId(tag.getTagId())
                        .post(postEntity)
                        .tag(tag.getTag())
                        .build()).toList());

        return postEntity;
    }

}
