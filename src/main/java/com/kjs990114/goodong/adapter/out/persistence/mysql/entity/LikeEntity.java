package com.kjs990114.goodong.adapter.out.persistence.mysql.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "likes")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "post_id"})})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    private Long postId;

    private Long userId;

}
