package com.kjs990114.goodong.application.port.in.post;

import com.kjs990114.goodong.adapter.in.web.dto.PostDTO.PostSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetPostsByPageUseCase {

    Page<PostSummaryDTO> getPostByPage(LoadPostsByPageCommand loadPostsByPageCommand);

    @Getter
    @AllArgsConstructor
    class LoadPostsByPageCommand {
        private Long userId;
        private Long viewerId;
        private Pageable pageable;
    }
}
