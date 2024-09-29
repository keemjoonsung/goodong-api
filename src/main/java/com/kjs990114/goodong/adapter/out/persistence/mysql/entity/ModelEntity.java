package com.kjs990114.goodong.adapter.out.persistence.mysql.entity;

import com.kjs990114.goodong.common.time.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "model")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    private Integer version;

    private String fileName;

    private String commitMessage;

}
