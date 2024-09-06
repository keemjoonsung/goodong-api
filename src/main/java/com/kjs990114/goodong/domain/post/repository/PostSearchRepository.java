package com.kjs990114.goodong.domain.post.repository;

import com.kjs990114.goodong.infrastructure.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {
    List<PostDocument> findByTitleContainingOrContentContainingOrTaggingContaining(String title, String content , String tagging);
    Optional<PostDocument> findByPostId(Long postId);
}