package com.kjs990114.goodong.adapter.out.persistence.entity;

import com.kjs990114.goodong.common.time.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "tag")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    private String tag;

}
