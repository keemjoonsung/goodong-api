package com.kjs990114.goodong.adapter.out.persistence.mysql.mapper;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.ModelEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.PostEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.TagEntity;
import com.kjs990114.goodong.domain.post.Model;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.post.Tag;

import java.util.stream.Collectors;

public class PostMapper {

    public static Post toDomain(PostEntity postEntity) {
        return Post.builder()
                .postId(postEntity.getPostId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .userId(postEntity.getUserId())
                .status(postEntity.getStatus())
                .createdAt(postEntity.getCreatedAt())
                .lastModifiedAt(postEntity.getLastModifiedAt())
                .models(postEntity.getModels().stream().map(
                        modelEntity -> Model.builder()
                                .modelId(modelEntity.getModelId())
                                .version(modelEntity.getVersion())
                                .fileName(modelEntity.getFileName())
                                .commitMessage(modelEntity.getCommitMessage())
                                .post(Post.of(postEntity.getPostId(), postEntity.getUserId()))
                                .build()).collect(Collectors.toList()))
                .tags(postEntity.getTags().stream().map(
                        tagEntity -> Tag.builder()
                                .tagId(tagEntity.getTagId())
                                .tag(tagEntity.getTag())
                                .post(Post.of(postEntity.getPostId(), postEntity.getUserId()))
                                .build()).collect(Collectors.toList()))
                .build();
    }

    public static PostEntity toEntity(Post post) {
        PostEntity postEntity = PostEntity.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUserId())
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

        postEntity.setTags(post.getTags().stream().map(
                tag -> TagEntity.builder()
                        .tagId(tag.getTagId())
                        .tag(tag.getTag())
                        .post(postEntity)
                        .build()).collect(Collectors.toList()));

        return postEntity;
    }

}
