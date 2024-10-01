package com.kjs990114.goodong.adapter.out.persistence.mysql.entity;

import com.kjs990114.goodong.common.time.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "comment")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String content;

}
