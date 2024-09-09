package com.kjs990114.goodong.presentation.endpoint.post;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.application.file.FileService;
import com.kjs990114.goodong.application.post.LikeService;
import com.kjs990114.goodong.application.post.PostService;
import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostEndpoint {

    private final PostService postService;
    private final JwtUtil jwtUtil;
    private final UserAuthService userAuthService;
    private final LikeService likeService;
    private final FileService fileService;

    //포스트 생성
    @PostMapping

    public CommonResponseEntity<Void> createPost(PostDTO.Create create,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long userId = userAuthService.getUserInfo(token).getUserId();
        postService.createPost(create, userId);
        return new CommonResponseEntity<>("Post created successfully");
    }

    // 유저의 post 리스트 반환
    @GetMapping
    public CommonResponseEntity<List<PostDTO.Summary>> getUserPosts(@RequestParam("userId") Long userId,
                                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long viewerId = userAuthService.getUserInfo(token).getUserId();
        List<PostDTO.Summary> response = new ArrayList<>(postService.getUserPublicPosts(userId));
        if (userId.equals(viewerId)) {
            response.addAll(postService.getUserPrivatePosts(userId));
        }
        response.sort(Comparator.comparing(PostDTO.Summary::getLastModifiedAt).reversed());

        return new CommonResponseEntity<>(response);
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
        Long userId = userAuthService.getUserInfo(token).getUserId();

        if (!jwtUtil.getEmail(token).equals(postService.getPost(postId).getEmail())) {
            throw new GlobalException("UnAuthorized Exception");
        }
        postService.updatePost(postId, userId, postDTO);
        return new CommonResponseEntity<>("Update success");
    }

    // 게시글 Delete
    @DeleteMapping("/{postId}")
    public CommonResponseEntity<Void> deletePost(@PathVariable("postId") Long postId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = userAuthService.getUserInfo(token).getUserId();

        if (!jwtUtil.getEmail(token).equals(postService.getPost(postId).getEmail())) {
            throw new GlobalException("UnAuthorized Exception");
        }
        postService.deletePost(userId, postId);
        return new CommonResponseEntity<>("Delete success");
    }

    // 특정 게시글 정보 가져오기
    @GetMapping("/{postId}")
    public CommonResponseEntity<PostDTO.PostDetail> getPost(@PathVariable("postId") Long postId,
                                                            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        Long viewerId = token == null ? null : userAuthService.getUserInfo(token).getUserId();

        if(!postService.getPost(postId).getUserId().equals(viewerId) && postService.getPost(postId).getStatus().equals(Post.PostStatus.PRIVATE)){
            throw new GlobalException("UnAuthorized Exception");
        }
        PostDTO.PostDetail postDetail = postService.getPost(postId);
        if(viewerId != null) {
            postDetail.setLiked(likeService.isLiked(postId,viewerId));
        }
        postDetail.setLikes(likeService.getLikesCount(postId));
        return new CommonResponseEntity<>(postDetail);
    }

    @GetMapping("/duplicated")
    public CommonResponseEntity<Boolean> isDuplicateTitle(@RequestParam("title") String title,
                                                          @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return new CommonResponseEntity<>(postService.checkDuplicatedTitle(title, token));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadModel(@RequestParam("fileName") String fileName) {
        Resource resource = fileService.getFileResource(fileName, FileService.Extension.GLB);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "model.glb")
                .body(resource);
    }


}
