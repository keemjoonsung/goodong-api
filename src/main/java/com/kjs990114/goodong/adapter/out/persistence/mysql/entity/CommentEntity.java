package com.kjs990114.goodong.adapter.out.persistence.mysql.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "comment")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private Long postId;

    private Long userId;

    private String content;

}
