package com.kjs990114.goodong.application.port.in.post;

import lombok.*;

public interface DeletePostUseCase {
    void deletePost(DeletePostCommand deletePostCommand);

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class DeletePostCommand{
        Long postId;
        Long userId;
    }
}
