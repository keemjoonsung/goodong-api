package com.kjs990114.goodong.application.port.in.post;

import com.kjs990114.goodong.adapter.in.web.dto.PostDTO.AiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public interface GeneratePostInfoByAiUseCase {

    AiResponse getPostInfo(GetPostInfoByAiQuery getPostInfoByAiQuery);

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class GetPostInfoByAiQuery{
        MultipartFile filePng;
    }
}
