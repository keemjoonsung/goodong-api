package com.kjs990114.goodong.adapter.out.persistence.elasticsearch;

import co.elastic.clients.elasticsearch._types.FieldValue;
import com.kjs990114.goodong.application.port.out.search.SearchPostPort;
import com.kjs990114.goodong.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchAdapter implements SearchPostPort {

    private final ElasticsearchOperations elasticsearchOperations;
    @Override
    public Page<Post> searchPost(String keyword, Pageable pageable) {
        System.out.println(pageable);
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(query -> query
                        .bool(bool -> bool
                                .should(should -> should.match(match -> match.field("title").query(FieldValue.of(keyword)).boost(3.0f)))
                                .should(should -> should.match(match -> match.field("tagging").query(FieldValue.of(keyword)).boost(2.0f)))
                                .should(should -> should.match(match -> match.field("content").query(FieldValue.of(keyword)).boost(1.0f)))
                        )
                )
                .withPageable(pageable)
                .build();
        System.out.println("ㅇㅇ");
        SearchHits<PostDocument> searchHits = elasticsearchOperations.search(searchQuery, PostDocument.class);
        System.out.println("ㅇㅇ1");
        List<Post> postIdList = searchHits.stream().map(hit -> Post.of(hit.getContent().getPostId())).toList();
        return new PageImpl<>(postIdList, pageable, searchHits.getTotalHits());

    }
}
