package com.kjs990114.goodong.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentInfoDTO {
    private Long commentId;
    private Long userId;
    private String email;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
