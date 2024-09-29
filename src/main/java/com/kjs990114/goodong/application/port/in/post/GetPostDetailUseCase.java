package com.kjs990114.goodong.application.port.in.post;

import com.kjs990114.goodong.adapter.in.web.dto.PostDTO.PostDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface GetPostDetailUseCase {

    PostDetailDTO getPostDetail(LoadPostDetailCommand loadPostDetailCommand);

    @Getter
    @AllArgsConstructor
    class LoadPostDetailCommand {
        private Long postId;
        private Long viewerId;
    }

}
