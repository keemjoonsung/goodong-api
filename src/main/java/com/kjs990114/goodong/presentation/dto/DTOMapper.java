package com.kjs990114.goodong.presentation.dto;

import com.kjs990114.goodong.domain.post.Model;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.post.Tag;
import com.kjs990114.goodong.domain.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {

    public static PostDTO.Summary postToSummary(Post post) {
        User user = post.getUser();
        List<Tag> tags = post.getTags();
        return PostDTO.Summary.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .status(post.getStatus())
                .lastModifiedAt(post.getLastModifiedAt())
                .tags(tags.stream().map(Tag::getTag).collect(Collectors.toList()))
                .likes(post.getLikeCount())
                .build();
    }

    public static PostDTO.PostDetail postToDetail(Post post) {
        List<Model> models = post.getModels();
        User user = post.getUser();
        List<PostDTO.ModelInfo> model = models.stream().map(m ->
                PostDTO.ModelInfo.builder()
                        .version(m.getVersion())
                        .fileName(m.getFileName())
                        .commitMessage(m.getCommitMessage())
                        .build()
        ).toList();
        return PostDTO.PostDetail.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .models(model)
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .tags(post.getTags().stream().map(Tag::getTag).collect(Collectors.toList()))
                .likes(post.getLikeCount())
                .build();
    }

    public static UserDTO.UserDetail userToDetail(User user) {
        return UserDTO.UserDetail.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }

    public static UserDTO.UserSummary userToSummary(User user){
        return UserDTO.UserSummary.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }
}
