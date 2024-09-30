package com.kjs990114.goodong.application.port.in.post;

import com.kjs990114.goodong.adapter.in.web.dto.PostDTO.PostMetadataDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface GeneratePostMetadataUseCase {

    PostMetadataDTO getPostMetadata(GetPostMetadataQuery getPostMetadataQuery) throws IOException;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class GetPostMetadataQuery {
        private MultipartFile filePng;
    }
}
