package com.kjs990114.goodong.presentation.endpoint.post;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.application.post.AiService;
import com.kjs990114.goodong.application.post.PostService;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiEndpoint {
    private final AiService aiService;
    private final PostService postService;
    private final UserAuthService userAuthService;

    @PostMapping
    public CommonResponseEntity<PostDTO.AiResponse> aiService(
            @RequestParam(defaultValue = "false") Boolean autoCreate,
            @RequestParam(defaultValue = "PUBLIC") String status,
            PostDTO.files files,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) throws Exception{
        MultipartFile filePng = files.getFile();
        MultipartFile fileGlb = files.getFileGlb();
        List<String> response = aiService.getDescription(filePng);
        PostDTO.AiResponse aiResponse = new PostDTO.AiResponse(response.get(0), response.get(1), List.of(response.get(2).split(",")));
        if(autoCreate) {
            Long userId = userAuthService.getUserInfo(token).getUserId();
            postService.createPost(
                    PostDTO.Create.builder()
                            .title(aiResponse.getTitle())
                            .content(aiResponse.getDescription())
                            .tags(aiResponse.getTags())
                            .file(fileGlb)
                            .status(Post.PostStatus.valueOf(status))
                            .build()
                    ,userId
            );
            return new CommonResponseEntity<>("GEMINI API CREATED REPOSITORY SUCCESSFUL");
        }else{
            return new CommonResponseEntity<>("Gemini API response successful.", aiResponse);
        }

    }

}
