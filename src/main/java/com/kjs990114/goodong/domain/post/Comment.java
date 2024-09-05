package com.kjs990114.goodong.domain.post;

import com.kjs990114.goodong.common.time.BaseTimeEntity;
import com.kjs990114.goodong.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;

    private String content;

}
