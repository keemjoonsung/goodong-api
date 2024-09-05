package com.kjs990114.goodong.domain.post;

import com.kjs990114.goodong.common.time.BaseTimeEntity;
import com.kjs990114.goodong.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "post")
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
    private User user;

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<Model> models = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "tag",cascade = CascadeType.ALL, orphanRemoval = true)
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
    public void changeStatus(PostStatus status) {
        this.status = status;
    }
    // 댓글 추가
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }
    // 댓글 삭제
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);  // 관계 해제
    }
    // 좋아요 추가
    public void addLike(Like like) {
        likes.add(like);
    }
    // 좋아요 삭제
    public void removeLike(Like like) {
        likes.remove(like);
    }
    // 게시물 수정 (제목과 내용 수정)
    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }
    // 게시물에 태그 추가
    public void addTag(Tag tag) {
        this.tags.add(tag);
    }
    // 게시물에 태그 추가
    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }
    // 게시물에 태그 clear
    public void removeTagAll(){
        this.tags.clear();
    }
    // 게시물에 태그 전체 추가
    public void addTagAll(List<Tag> tags) {
        this.tags.addAll(tags);
    }
    // 게시글에 모델 추가
    public void addModel(Model model) {
        this.models.add(model);    // 모델 추가
    }
    // 기존 모델 중 가장 높은 버전을 찾아 다음 버전 계산
    public short getNextModelVersion() {
        return (short) (models.stream()
                        .mapToInt(Model::getVersion)  // 각 모델의 버전을 가져옴
                        .max()  // 가장 높은 버전 찾기
                        .orElse(0) + 1);  // 만약 모델이 없으면 버전 1로 시작
    }
}






