package com.kjs990114.goodong.adapter.out.persistence.entity;
import com.kjs990114.goodong.common.time.BaseTimeEntity;
import com.kjs990114.goodong.domain.post.*;
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
public class PostEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ModelEntity> models = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LikeEntity> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TagEntity> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PostStatus status = PostStatus.PUBLIC;

    public enum PostStatus {
        PUBLIC,
        PRIVATE,
    }


}