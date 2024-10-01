package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.dto.PostDTO.PostMetadataDTO;
import com.kjs990114.goodong.application.port.in.post.GeneratePostMetadataUseCase;
import com.kjs990114.goodong.application.port.out.ai.GeneratePostMetadataPort;
import com.kjs990114.goodong.application.port.out.ai.GeneratePostMetadataPort.PostAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GeneratePostMetadataService implements GeneratePostMetadataUseCase {

    private final GeneratePostMetadataPort generatePostMetadataPort;

    @Transactional
    @Override
    public PostMetadataDTO getPostMetadata(GetPostMetadataQuery getPostMetadataQuery) throws IOException {
        PostAttributes postAttributes = generatePostMetadataPort.generatePost(getPostMetadataQuery.getFilePng());
        return new PostMetadataDTO(postAttributes.getTitle(), postAttributes.getContent(), postAttributes.getTags());
    }
}
