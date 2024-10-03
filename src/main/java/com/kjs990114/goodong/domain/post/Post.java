package com.kjs990114.goodong.domain.post;

import com.kjs990114.goodong.domain.comment.Comment;
import com.kjs990114.goodong.domain.like.Like;
import com.kjs990114.goodong.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    private Long postId;
    private String title;
    private String content;
    private Long userId;  // 도메인 객체로 참조
    @Builder.Default
    private List<Model> models = new ArrayList<>();
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();
    @Builder.Default
    private PostStatus status = PostStatus.PUBLIC;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;


    public enum PostStatus {
        PUBLIC,
        PRIVATE,
    }
    public static Post of(Long postId, Long userId){
        return Post.builder().postId(postId).userId(userId).build();
    }
    public void updateTitle(String title){
        if(title != null) {
            this.title = title;
        }
    }
    public void updateContent(String content){
        if(content != null) {
            this.content = content;
        }
    }
    public void updateStatus(PostStatus status) {
        if(status != null) {
            this.status = status;
        }
    }
    // 게시물에 태그 전체 추가
    public void updateTag(List<String> tags) {
        if (tags != null) {
            removeTagAll();
            for (String tag : tags) {
                Tag newTag = new Tag(null, this, tag);
                this.tags.add(newTag);
            }
        }
    }

    // 게시글에 모델 추가
    public void addModel(String fileName, String commitMsg) {
        if(fileName.isBlank() || fileName.isEmpty()) return;
        int version = getNextModelVersion();
        Model newModel = Model.builder()
                .fileName(fileName)
                .commitMessage(commitMsg)
                .version(version)
                .post(this)
                .build();
        this.models.add(newModel);
    }

    // 게시물에 태그 clear
    private void removeTagAll() {
        this.tags.clear();
    }

    // 모델의 다음 버전 계산
    private int getNextModelVersion() {
        return models.stream()
                .mapToInt(Model::getVersion)
                .max()
                .orElse(0) + 1;
    }
}