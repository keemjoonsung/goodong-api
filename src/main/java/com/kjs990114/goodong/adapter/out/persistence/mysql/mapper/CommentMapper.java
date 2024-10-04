package com.kjs990114.goodong.adapter.out.persistence.mysql.mapper;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.CommentEntity;
import com.kjs990114.goodong.domain.comment.Comment;

public class CommentMapper {

    public static CommentEntity toEntity(Comment comment){
        return CommentEntity.builder()
                .commentId(comment.getCommentId())
                .userId(comment.getUserId())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .build();
    }
    public static Comment toDomain(CommentEntity commentEntity){
        return Comment.builder()
                .commentId(commentEntity.getCommentId())
                .userId(commentEntity.getUserId())
                .postId(commentEntity.getPostId())
                .content(commentEntity.getContent())
                .build();
    }
}
