package com.kjs990114.goodong.domain.user;


import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contribution {

    private Long contId;
    private LocalDate date = LocalDate.now();
    private User user;
    private Integer count = 1;
}