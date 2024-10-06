package com.kjs990114.goodong.domain.follow;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Follow {
    private Long followId;
    private Long followerId;
    private Long followeeId;

    public static Follow of(Long f1, Long f2){
        return Follow.builder()
                .followerId(f1)
                .followeeId(f2)
                .build();
    }
}