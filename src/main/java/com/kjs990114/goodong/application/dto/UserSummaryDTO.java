package com.kjs990114.goodong.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserSummaryDTO {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImage;
}
