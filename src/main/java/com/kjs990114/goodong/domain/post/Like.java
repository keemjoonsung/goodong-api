package com.kjs990114.goodong.domain.post;

import com.kjs990114.goodong.common.time.BaseTimeEntity;
import com.kjs990114.goodong.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "likes")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "post_id"})})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
