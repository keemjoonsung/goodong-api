package com.kjs990114.goodong.application.port.out.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GeneratePostMetadataPort {
    PostAttributes generatePost(MultipartFile filePng) throws IOException;

    @Getter
    @AllArgsConstructor
    class PostAttributes {
        private String title;
        private String content;
        private List<String> tags;
    }
}
