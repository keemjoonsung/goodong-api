package com.kjs990114.goodong.presentation.endpoint;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.application.post.PostService;
import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostEndpoint {

    private final PostService postService;
    private final JwtUtil jwtUtil;
    private final UserAuthService userAuthService;

    //포스트 생성
    @PostMapping
    public CommonResponseEntity<Void> createPost(PostDTO.Create create,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        String email = jwtUtil.getEmail(token);
        postService.createPost(create, email);
        return new CommonResponseEntity<>("Post created successfully");
    }

    // 유저의 post 리스트 반환
    @GetMapping
    public CommonResponseEntity<List<PostDTO.Summary>> getUserPosts(@RequestParam("userId") Long userId,
                                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        boolean isMyPosts = userAuthService.getUserInfo(token).getUserId().equals(userId);
        return new CommonResponseEntity<>(postService.getUserPosts(userId, isMyPosts));
    }

    //검색 -> elastic search
    @GetMapping("/search")
    public CommonResponseEntity<List<PostDTO.Summary>> searchPosts(@RequestParam("keyword") String keyword) {
        return new CommonResponseEntity<>(postService.searchPosts(keyword));
    }


    // 게시글 Update
    @PatchMapping("/{postId}")
    public CommonResponseEntity<Void> updatePost(@PathVariable("postId") Long postId,
                                                 @RequestBody PostDTO.Update postDTO,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long viewerId = userAuthService.getUserInfo(token).getUserId();

        if (!jwtUtil.getEmail(token).equals(postService.getPost(postId, viewerId).getEmail())) {
            throw new GlobalException("UnAuthorized Exception");
        }
        postService.updatePost(postId, postDTO);
        return new CommonResponseEntity<>("Update success");
    }

    // 게시글 Delete
    @DeleteMapping("/{postId}")
    public CommonResponseEntity<Void> deletePost(@PathVariable("postId") Long postId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long viewerId = userAuthService.getUserInfo(token).getUserId();

        if (!jwtUtil.getEmail(token).equals(postService.getPost(postId , viewerId).getEmail())) {
            throw new GlobalException("UnAuthorized Exception");
        }
        postService.deletePost(postId);
        return new CommonResponseEntity<>("Delete success");
    }

    // 특정 게시글 정보 가져오기
    @GetMapping("/{postId}")
    public CommonResponseEntity<PostDTO.PostDetail> getPost(@PathVariable("postId") Long postId,
                                                            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        Long viewerId = token == null ? null : userAuthService.getUserInfo(token).getUserId();
        return new CommonResponseEntity<>(postService.getPost(postId, viewerId));
    }
    @GetMapping("/duplicated")
    public CommonResponseEntity<Boolean> isDuplicateTitle(@RequestParam("title") String title,
                                                          @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token){
        return new CommonResponseEntity<>(postService.checkDuplicatedTitle(title,token));
    }
    // 파일 url로 다운로드
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadModel(@RequestParam("fileUrl") String fileUrl) {
        Resource resource = postService.getFileResource(fileUrl);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "model.glb")
                .body(resource);
    }


}
