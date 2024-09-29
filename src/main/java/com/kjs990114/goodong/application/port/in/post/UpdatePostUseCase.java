package com.kjs990114.goodong.application.port.in.post;

import com.kjs990114.goodong.domain.post.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface UpdatePostUseCase {

    void updatePost(UpdatePostCommand updatePostCommand) throws IOException;

    @Getter
    @Builder
    @AllArgsConstructor
    class UpdatePostCommand{
        private Long postId;
        private Long userId;
        @NotBlank(message = "title cannot be blank")
        private String title;
        @NotBlank(message = "content cannot be blank")
        private String content;
        private MultipartFile file;
        @NotBlank(message = "commit message cannot be blank")
        private String commitMessage;
        private Post.PostStatus status;
        @Builder.Default
        private List<String> tags = new ArrayList<>();
    }
}
