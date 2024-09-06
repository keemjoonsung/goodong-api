package com.kjs990114.goodong.presentation.endpoint;

import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AIEndpoint {

    @GetMapping("/tags")
    public CommonResponseEntity<PostDTO.AiResponse> aiService(
            @RequestParam("imageUrl") String image
    ) {
        return new CommonResponseEntity<>(new PostDTO.AiResponse(List.of("태그 1", "태그 2", "태그 3")));
    }



}
