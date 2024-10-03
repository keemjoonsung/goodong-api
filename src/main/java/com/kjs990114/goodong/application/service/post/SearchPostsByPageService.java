package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.dto.PostSummaryDTO;
import com.kjs990114.goodong.application.port.in.post.SearchPostsByPageUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadPostPort;
import com.kjs990114.goodong.application.port.out.search.SearchPostPort;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchPostsByPageService implements SearchPostsByPageUseCase {

    private final SearchPostPort searchPostPort;
    private final LoadPostPort loadPostPort;

    @Transactional(readOnly = true)
    @Override
    public Page<PostSummaryDTO> searchPostsByPage(SearchPostsByPageQuery searchPostsByPageQuery) {
        String query = searchPostsByPageQuery.getQuery();
        Pageable pageable = searchPostsByPageQuery.getPageable();

        Page<Post> tmpPage = searchPostPort.searchPost(query,pageable);
        List<Long> postIdList = tmpPage.stream().map(Post::getPostId).toList();
        Page<Post> postsList = new PageImpl<>(loadPostPort.loadByPostIds(postIdList), pageable, tmpPage.getTotalElements());
//        return postsList.map(PostSummaryDTO::of);
        return null;

    }
}
