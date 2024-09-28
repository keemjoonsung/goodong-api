package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.application.service.*;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.adapter.out.persistence.entity.PostEntity;
import com.kjs990114.goodong.adapter.in.web.dto.ApiResponse;
import com.kjs990114.goodong.adapter.in.web.dto.PostDTO;

import com.kjs990114.goodong.adapter.in.web.dto.RestPage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostEndpoint {

    private final PostService postService;
    private final UserAuthService userAuthService;
    private final LikeService likeService;
    private final CommentService commentService;

    //포스트 생성
    @PostMapping
    public ApiResponse<Void> createPost(@Valid PostDTO.Create create,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long userId = userAuthService.getUserId(token);
        postService.createPost(create, userId);
        return new ApiResponse<>("Post created successfully");
    }

    // 유저의 posts
    @GetMapping(params = "userId")
    public ApiResponse<RestPage<PostDTO.Summary>> getUserPosts(@RequestParam(name = "userId") Long userId,
                                                               @RequestHeader(required = false, name = HttpHeaders.AUTHORIZATION) String token,
                                                               @RequestParam(name = "page", defaultValue = "0") int page) {
        Long viewerId = token == null ? null : userAuthService.getUserId(token);
        boolean isMyPosts = userId.equals(viewerId);
        RestPage<PostDTO.Summary> response = postService.getPosts(userId, page, isMyPosts);
        return new ApiResponse<>(response);
    }

    // posts 검색
    @GetMapping(params = "query")
    public ApiResponse<Page<PostDTO.Summary>> searchPosts(@RequestParam("query") String keyword,
                                                          @RequestParam(name = "page", defaultValue = "0") int page) {
        return new ApiResponse<>(postService.searchPosts(keyword, page));
    }

    @GetMapping("/all")
    public ApiResponse<List<PostDTO.Summary>> getUserPosts(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long viewerId = userAuthService.getUserId(token);
        List<PostDTO.Summary> response = postService.getMyPosts(viewerId);
        return new ApiResponse<>(response);
    }

    // 게시글 Update
    @PatchMapping("/{postId}")
    public ApiResponse<Void> updatePost(@PathVariable("postId") Long postId,
                                        PostDTO.Update postDTO,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long userId = userAuthService.getUserId(token);
        if (!userId.equals(postService.getPost(postId).getUserId())) {
            throw new UnAuthorizedException("UnAuthorized Exception");
        }
        postService.updatePost(postId, postDTO);
        return new ApiResponse<>("Update success");
    }

    // 게시글 Delete
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable("postId") Long postId,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = userAuthService.getUserId(token);
        if (!userId.equals(postService.getPost(postId).getUserId())) {
            throw new UnAuthorizedException("UnAuthorized Exception");
        }
        postService.deletePost(postId);
        return new ApiResponse<>("Delete success");
    }

    // 특정 게시글 정보 가져오기
    @GetMapping("/{postId}")
    public ApiResponse<PostDTO.PostDetail> getPost(@PathVariable("postId") Long postId,
                                                   @RequestHeader(required = false, value = HttpHeaders.AUTHORIZATION) String token
    ) {
        Long viewerId = token == null ? null : userAuthService.getUserId(token);
        if (!postService.getPost(postId).getUserId().equals(viewerId) && postService.getPost(postId).getStatus().equals(PostEntity.PostStatus.PRIVATE)) {
            throw new UnAuthorizedException("UnAuthorized Exception");
        }
        PostDTO.PostDetail postDetail = postService.getPost(postId);
        if (viewerId != null) {
            postDetail.setLiked(likeService.isLiked(postId, viewerId));
        }
        postDetail.setComments(commentService.getComments(postId));
        return new ApiResponse<>(postDetail);
    }

    //중복체크
    @GetMapping("/check-title")
    public ApiResponse<Boolean> isDuplicateTitle(@RequestParam("title") String title,
                                                 @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = userAuthService.getUserId(token);
        return new ApiResponse<>(postService.checkDuplicatedTitle(title, userId));
    }

    // 댓글 달기
    @PostMapping("/{postId}/comments")  // 댓글 달
    public ApiResponse<Void> addComment(@PathVariable("postId") Long postId,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody PostDTO.PostComment postComment) {
        String email = jwtUtil.getEmail(token);
        String content = postComment.getContent();
        commentService.addComment(postId, email, content);

        return new ApiResponse<>("Comment added successfully");
    }

    //댓글 삭제 하기
    @DeleteMapping("/{postId}/comments")
    public ApiResponse<Void> deleteComment(@PathVariable("postId") Long postId,
                                           @RequestParam("commentId") Long commentId,
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String email = jwtUtil.getEmail(token);
        commentService.deleteComment(postId, commentId, email);
        return new ApiResponse<>("Comment deleted successfully");
    }

    //댓글 업데이트 하기
    @PatchMapping("/{postId}/comments")
    public ApiResponse<Void> updateComment(@PathVariable("postId") Long postId,
                                           @RequestParam("commentId") Long commentId,
                                           @RequestBody PostDTO.PostComment postComment, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String email = jwtUtil.getEmail(token);
        String content = postComment.getContent();
        commentService.updateComment(postId,commentId, email, content);
        return new ApiResponse<>("Comment updated successfully");

    }
    // 포스트의 model 다운로드
    @GetMapping("/models")
    public ResponseEntity<Resource> downloadModel(@RequestParam("modelName") String modelName,
                                                  @RequestHeader(required = false, name = HttpHeaders.AUTHORIZATION) String token) {
        PostDTO.PostInfo post = postService.getPost(modelName);
        Long userId = token == null ? null : userAuthService.getUserId(token);
        if (((post.getStatus() == PostEntity.PostStatus.PRIVATE) && !(post.getUserId().equals(userId)))) {
            throw new UnAuthorizedException("UnAuthorized Exception");
        }
        Resource resource = fileService.getFileResource(modelName, FileService.Extension.GLB);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "model.glb")
                .body(resource);
    }
    //ai이용 포스트 생성
    @PostMapping("/ai")
    public ApiResponse<PostDTO.AiResponse> aiService(
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
            Long userId = userAuthService.getUserId(token);
            postService.createPost(
                    PostDTO.Create.builder()
                            .title(aiResponse.getTitle())
                            .content(aiResponse.getDescription())
                            .tags(aiResponse.getTags())
                            .file(fileGlb)
                            .status(PostEntity.PostStatus.valueOf(status))
                            .build()
                    ,userId
            );
            return new ApiResponse<>("GEMINI API CREATED REPOSITORY SUCCESSFUL");
        }else{
            return new ApiResponse<>("Gemini API response successful.", aiResponse);
        }

    }



}
