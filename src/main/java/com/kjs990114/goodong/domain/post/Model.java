package com.kjs990114.goodong.domain.post;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Model {

    private Long modelId;
    private Post post;
    private Integer version;
    private String fileName;
    private String commitMessage;
}