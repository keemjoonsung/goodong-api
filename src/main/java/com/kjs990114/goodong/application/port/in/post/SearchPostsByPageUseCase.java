package com.kjs990114.goodong.application.port.in.post;

import com.kjs990114.goodong.application.dto.PostSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchPostsByPageUseCase {
    Page<PostSummaryDTO> searchPostsByPage(SearchPostsByPageQuery searchPostsByPageQuery);

    @Getter
    @AllArgsConstructor
    class SearchPostsByPageQuery{
        private String query;
        private Pageable pageable;
    }
}
