package com.kjs990114.goodong.adapter.out.persistence.mysql.entity;

import com.kjs990114.goodong.common.time.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "likes")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "post_id"})})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
