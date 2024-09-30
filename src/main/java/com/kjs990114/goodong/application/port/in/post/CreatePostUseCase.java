package com.kjs990114.goodong.application.port.in.post;

import com.kjs990114.goodong.domain.post.Post.PostStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface CreatePostUseCase {

    void createPost(CreatePostCommand createPostCommand) throws IOException;

    @Getter
    @Builder
    @AllArgsConstructor
    class CreatePostCommand{
        private Long userId;
        @NotBlank(message = "title cannot be blank")
        private String title;
        @NotBlank(message = "Content cannot be blank")
        private String content;
        private MultipartFile file;
        private PostStatus status;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
    }
}
