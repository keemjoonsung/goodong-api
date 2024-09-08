package com.kjs990114.goodong.domain.post;

import com.kjs990114.goodong.common.time.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Model extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private Integer version;

    private String fileName;

    private String commitMessage;

}
