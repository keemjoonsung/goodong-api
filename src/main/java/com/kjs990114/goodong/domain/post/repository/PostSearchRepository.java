package com.kjs990114.goodong.domain.post.repository;

import com.kjs990114.goodong.infrastructure.post.elasticsearch.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {
    List<PostDocument> findByTitleContainingOrContentContainingOrTaggingContaining(String title, String content , String tagging);
}