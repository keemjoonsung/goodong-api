package com.kjs990114.goodong.domain.post;

import com.kjs990114.goodong.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    public enum PostStatus {
        PUBLIC,
        PRIVATE,
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



    // 게시물에 태그 clear
    public void removeTagAll() {
        this.tags.clear();
    }

    // 게시물에 태그 전체 추가
    public void addTagAll(List<String> tags) {
        if (tags != null) {
            for (String tag : tags) {
                Tag newTag = new Tag(null, this, tag);
                this.tags.add(newTag);
            }
        }
    }

    // 게시글에 모델 추가
    public void addModel(Model model) {
        this.models.add(model);    // 모델 추가
    }

    // 모델의 다음 버전 계산
    public int getNextModelVersion() {
        return models.stream()
                .mapToInt(Model::getVersion)
                .max()
                .orElse(0) + 1;
    }
}