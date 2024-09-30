package com.kjs990114.goodong.application.port.in.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public interface CreatePostByAiUseCase {

    void createPostByAi(CreatePostByAiCommand createPostByAiCommand);

    @Getter
    @AllArgsConstructor
    class CreatePostByAiCommand{
        private Long userId;
        private MultipartFile filePng;
        private MultipartFile fileGlb;
    }
}
