package com.kjs990114.goodong.adapter.out.persistence.mysql.mapper;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.*;
import com.kjs990114.goodong.domain.post.*;
import com.kjs990114.goodong.domain.user.User;

public class PostMapper {

    public static Post toDomain(PostEntity postEntity) {
        return Post.builder()
                .postId(postEntity.getPostId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .user(User.of(postEntity.getUser().getUserId()))
                .status(postEntity.getStatus())
                .models(postEntity.getModels().stream().map(
                        modelEntity -> Model.builder()
                                .modelId(modelEntity.getModelId())
                                .version(modelEntity.getVersion())
                                .fileName(modelEntity.getFileName())
                                .commitMessage(modelEntity.getCommitMessage())
                                .post(Post.of(modelEntity.getPost().getPostId()))
                                .build()).toList())
                .comments(postEntity.getCommentEntities().stream().map(
                        commentEntity -> Comment.builder()
                                .commentId(commentEntity.getCommentId())
                                .content(commentEntity.getContent())
                                .post(Post.of(commentEntity.getPost().getPostId()))
                                .user(User.of(commentEntity.getUser().getUserId()))
                                .build()).toList())
                .likes(postEntity.getLikes().stream().map(
                        likeEntity -> Like.builder()
                                .likeId(likeEntity.getLikeId())
                                .user(User.of(likeEntity.getUser().getUserId()))
                                .post(Post.of(likeEntity.getPost().getPostId()))
                                .build()).toList())
                .tags(postEntity.getTags().stream().map(
                        tagEntity -> Tag.builder()
                                .tagId(tagEntity.getTagId())
                                .post(Post.of(tagEntity.getPost().getPostId()))
                                .tag(tagEntity.getTag())
                                .build()).toList())
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

        postEntity.setCommentEntities(post.getComments().stream().map(
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
