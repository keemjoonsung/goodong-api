package com.kjs990114.goodong.domain.post;

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
    private User user;  // 도메인 객체로 참조
    @Builder.Default
    private List<Model> models = new ArrayList<>();
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
    @Builder.Default
    private List<Like> likes = new ArrayList<>();
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
    public static Post of(Long postId){
        return Post.builder().postId(postId).build();
    }
    public void updateTitle(String title){
        this.title = title;
    }
    public void updateContent(String content){
        this.content = content;
    }
    public void updateStatus(PostStatus status) {
         this.status = status;
    }
    public void updateOwner(User user){
        this.user = user;
    }
    public int getLikeCount() {
        return likes.size();
    }

    // 댓글 추가
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    // 댓글 삭제
    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    // 좋아요 추가
    public void like(Like like) {
        likes.add(like);
    }

    // 좋아요 삭제
    public void unLike(Like like) {
        likes.remove(like);
    }
    public boolean isLikedBy(Long userId){
        return this.likes.stream().anyMatch(like -> like.getUser().getUserId().equals(userId));
    }


    // 게시물에 태그 clear
    private void removeTagAll() {
        this.tags.clear();
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
        System.out.println("실행");
        System.out.println("fileName = " + fileName);
        if(fileName.isBlank() || fileName.isEmpty()) return;
        int version = getNextModelVersion();
        Model newModel = Model.builder()
                .fileName(fileName)
                .commitMessage(commitMsg)
                .version(version)
                .post(this)
                .build();
        this.models.add(newModel);
        this.user.updateContribution();
        System.out.println("user의 contribution: " + user.getContributions());
    }
    // 모델의 다음 버전 계산
    private int getNextModelVersion() {
        return models.stream()
                .mapToInt(Model::getVersion)
                .max()
                .orElse(0) + 1;
    }
}