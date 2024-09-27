package com.kjs990114.goodong.domain.post;

import com.kjs990114.goodong.common.time.BaseTimeEntity;
import com.kjs990114.goodong.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "post")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "user_id"})})
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Model> models = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PostStatus status = PostStatus.PUBLIC;

    public enum PostStatus {
        PUBLIC,
        PRIVATE,
    }

    // 게시물의 공개 상태를 변경하는 메서드
    public void updateStatus(PostStatus status) {
        if (status != null) this.status = status;
    }
    public int getLikeCount(){
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

    // 게시물 수정 (제목과 내용 수정)
    public void updatePost(String title, String content) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
    }

    // 게시물에 태그 clear
    public void removeTagAll() {
        this.tags.clear();
    }

    // 게시물에 태그 전체 추가
    public void addTagAll(List<String> tags) {
        if (tags != null) {
            for (String tag : tags) {
                Tag newTag = Tag.builder()
                        .tag(tag)
                        .post(this)
                        .build();
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






