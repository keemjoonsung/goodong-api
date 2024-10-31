package com.kjs990114.goodong.adapter.out.persistence.mysql.entity;

import com.kjs990114.goodong.domain.post.Post.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "post")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "user_id","is_available"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PostStatus status = PostStatus.PUBLIC;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ModelEntity> models = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<TagEntity> tags = new ArrayList<>();

    /**
     alter table user add is_available BOOLEAN GENERATED ALWAYS AS ( CASE WHEN deleted_at IS NULL THEN 1 ELSE NULL END);
     **/
    @Column(insertable = false, updatable = false)
    private Boolean isAvailable;


    public static PostEntity of(Long postId) {
        return PostEntity.builder().postId(postId).build();
    }

}