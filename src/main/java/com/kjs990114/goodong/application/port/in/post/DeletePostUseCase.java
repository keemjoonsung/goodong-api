package com.kjs990114.goodong.application.port.in.post;

import lombok.*;

public interface DeletePostUseCase {
    void deletePost(DeletePostCommand deletePostCommand);

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class DeletePostCommand{
        private Long postId;
        private Long userId;
    }
}
