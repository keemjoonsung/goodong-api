package com.kjs990114.goodong.presentation.endpoint.post;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.application.file.FileService;
import com.kjs990114.goodong.application.post.PostService;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
public class ModelEndpoint {
    private final PostService postService;
    private final UserAuthService userAuthService;
    private final FileService fileService;

    @GetMapping(params = "modelName")
    public ResponseEntity<Resource> downloadModel(@RequestParam("modelName") String modelName,
                                                  @RequestHeader(required = false, name = HttpHeaders.AUTHORIZATION) String token) {
        PostDTO.PostInfo post = postService.getPost(modelName);
        Long userId = token == null ? null : userAuthService.getUserId(token);
        if (((post.getStatus() == Post.PostStatus.PRIVATE) && !(post.getUserId().equals(userId)))) {
            throw new UnAuthorizedException("UnAuthorized Exception");
        }
        Resource resource = fileService.getFileResource(modelName, FileService.Extension.GLB);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "model.glb")
                .body(resource);
    }
}
