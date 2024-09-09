package com.kjs990114.goodong.presentation.endpoint.post;

import com.kjs990114.goodong.application.post.AIService;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIEndpoint {
    private final AIService aiService;
    @PostMapping
    public CommonResponseEntity<PostDTO.AiResponse> aiService(
             PostDTO.File file
    ) throws Exception{
        List<String> response = aiService.getDescription(file.getFile());

        return new CommonResponseEntity<>("Gemini API response successful.",new PostDTO.AiResponse(response.get(0),response.get(1),List.of(response.get(2).split(","))));
    }

}
