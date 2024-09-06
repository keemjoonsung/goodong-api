package com.kjs990114.goodong.presentation.endpoint;

import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AIEndpoint {

    @PostMapping("/description")
    public CommonResponseEntity<AiResponse> aiService(
            @RequestBody ImageRequest image
    ) {
        return new CommonResponseEntity<>(new AiResponse(List.of("태그 1", "태그 2", "태그 3")));
    }

    public static class ImageRequest {
        private String ImageUrl;
    }
    public static class AiResponse {
        private List<String> tags;
        public AiResponse(List<String> tags) {}
    }


}
