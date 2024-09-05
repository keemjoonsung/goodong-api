package com.kjs990114.goodong.presentation.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
@Builder
public class PostDTO {


    @Getter
    public static class CreateDTO{
        private String title;
        private String content;
        private MultipartFile file;
    }

    @Getter
    @Setter
    public static class SummaryDTO {
        private Long postId;
        private String title;
        private String ownerEmail;
        private String ownerNickname;
        private LocalDateTime lastModifiedAt;
        private List<String> tags;
    }

    @Getter
    @Builder
    public static class DetailDTO {
        private Long postId;
        private String title;
        private String content;
        private List<ModelDTO> models;
        private String ownerEmail;
        private String ownerNickname;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private List<String> tags;
    }
    @Getter
    @Builder
    public static class ModelDTO {
        private Short version;
        private String fileUrl;
        private String commitMessage;
    }

}
