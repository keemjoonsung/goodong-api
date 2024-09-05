package com.kjs990114.goodong.domain.post;

import com.kjs990114.goodong.common.time.BaseTimeEntity;
import com.kjs990114.goodong.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "likes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;

}
