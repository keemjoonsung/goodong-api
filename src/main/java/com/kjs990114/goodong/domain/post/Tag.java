package com.kjs990114.goodong.domain.post;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    private Long tagId;
    private Post post;
    private String tag;
}