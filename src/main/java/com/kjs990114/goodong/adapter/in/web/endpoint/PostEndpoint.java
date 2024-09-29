package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.adapter.in.web.dto.ApiResponse;
import com.kjs990114.goodong.adapter.in.web.dto.PostDTO;
import com.kjs990114.goodong.adapter.in.web.dto.PostDTO.PostDetailDTO;
import com.kjs990114.goodong.adapter.in.web.dto.PostDTO.PostSummaryDTO;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase.TokenQuery;
import com.kjs990114.goodong.application.port.in.post.*;
import com.kjs990114.goodong.application.port.in.post.CreatePostUseCase.CreatePostCommand;
import com.kjs990114.goodong.application.port.in.post.DeletePostUseCase.DeletePostCommand;
import com.kjs990114.goodong.application.port.in.post.GetPostDetailUseCase.LoadPostDetailCommand;
import com.kjs990114.goodong.application.port.in.post.GetPostsByPageUseCase.LoadPostsByPageCommand;
import com.kjs990114.goodong.application.port.in.post.SearchPostsByPageUseCase.SearchPostsByPageQuery;
import com.kjs990114.goodong.application.port.in.post.UpdatePostUseCase.UpdatePostCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostEndpoint {

    private final CheckTokenUseCase checkTokenUseCase;
    private final CreatePostUseCase createPostUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    private final DeletePostUseCase deletePostUseCase;
    private final GetPostDetailUseCase getPostDetailUseCase;
    private final GetPostsByPageUseCase getPostsByPageUseCase;
    private final SearchPostsByPageUseCase searchPostsByPageUseCase;

    @Value("${spring.page.size}")
    private int pageSize;

    //게시글 Create
    @PostMapping
    public ApiResponse<Void> createPost(@Valid PostDTO.PostCreateDTO postCreateDTO,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        CreatePostCommand createPostCommand = CreatePostCommand.builder()
                .userId(userId)
                .title(postCreateDTO.getTitle())
                .content(postCreateDTO.getContent())
                .file(postCreateDTO.getFile())
                .status(postCreateDTO.getStatus())
                .tags(postCreateDTO.getTags())
                .build();
        createPostUseCase.createPost(createPostCommand);
        return new ApiResponse<>("Post created successfully");
    }

    // 게시글 Update
    @PatchMapping("/{postId}")
    public ApiResponse<Void> updatePost(@PathVariable("postId") Long postId,
                                        PostDTO.PostUpdateDTO postDTO,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        UpdatePostCommand updatePostCommand = UpdatePostCommand.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .commitMessage(postDTO.getCommitMessage())
                .status(postDTO.getStatus())
                .file(postDTO.getFile())
                .tags(postDTO.getTags())
                .postId(postId)
                .userId(userId)
                .build();
        updatePostUseCase.updatePost(updatePostCommand);
        return new ApiResponse<>("Update success");
    }

    //게시글 Delete
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable("postId") Long postId,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        DeletePostCommand deletePostCommand = new DeletePostCommand(postId, userId);
        deletePostUseCase.deletePost(deletePostCommand);
        return new ApiResponse<>("Delete success");
    }

    // 특정 게시글 정보 가져오기
    @GetMapping("/{postId}")
    public ApiResponse<PostDetailDTO> getPost(@PathVariable("postId") Long postId,
                                              @RequestHeader(required = false, value = HttpHeaders.AUTHORIZATION) String token
    ) {
        Long viewerId = token == null ? null : checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        PostDetailDTO response = getPostDetailUseCase.getPostDetail(new LoadPostDetailCommand(postId, viewerId));

        return new ApiResponse<>(response);
    }

    @GetMapping
    public ApiResponse<Page<PostSummaryDTO>> getUserPosts(@RequestParam(required = false, name = "userId") Long userId,
                                                          @RequestParam(required = false, name = "query") String query,
                                                          @RequestHeader(required = false, name = HttpHeaders.AUTHORIZATION) String token,
                                                          @RequestParam(name = "all", defaultValue = "false") boolean allPage,
                                                          @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<PostSummaryDTO> response;
        Pageable pageable = allPage ? Pageable.unpaged(Sort.by("lastModifiedAt").descending()) : PageRequest.of(page, pageSize, Sort.by("lastModifiedAt").descending());
        if(query != null && !query.isEmpty() && !query.isBlank()){
            response = searchPostsByPageUseCase.searchPostsByPage(new SearchPostsByPageQuery(query, pageable));
        }else {
            Long viewerId = token == null ? null : checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
            Long ownerId = userId == null ? viewerId : userId;
            response = getPostsByPageUseCase.getPostByPage(new LoadPostsByPageCommand(ownerId, viewerId, pageable));
        }

        return new ApiResponse<>(response);
    }


//    //중복체크
//    @GetMapping("/check-title")
//    public ApiResponse<Boolean> isDuplicateTitle(@RequestParam("title") String title,
//                                                 @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
//        Long userId = userAuthService.getUserId(token);
//        return new ApiResponse<>(postService.checkDuplicatedTitle(title, userId));
//    }
//
//    // 댓글 달기
//    @PostMapping("/{postId}/comments")  // 댓글 달
//    public ApiResponse<Void> addComment(@PathVariable("postId") Long postId,
//                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody PostDTO.CommentDTO commentDTO) {
//        String email = jwtUtil.getEmail(token);
//        String content = commentDTO.getContent();
//        commentService.addComment(postId, email, content);
//
//        return new ApiResponse<>("Comment added successfully");
//    }
//
//    //댓글 삭제 하기
//    @DeleteMapping("/{postId}/comments")
//    public ApiResponse<Void> deleteComment(@PathVariable("postId") Long postId,
//                                           @RequestParam("commentId") Long commentId,
//                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//        String email = jwtUtil.getEmail(token);
//        commentService.deleteComment(postId, commentId, email);
//        return new ApiResponse<>("Comment deleted successfully");
//    }
//
//    //댓글 업데이트 하기
//    @PatchMapping("/{postId}/comments")
//    public ApiResponse<Void> updateComment(@PathVariable("postId") Long postId,
//                                           @RequestParam("commentId") Long commentId,
//                                           @RequestBody PostDTO.CommentDTO postComment, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//        String email = jwtUtil.getEmail(token);
//        String content = postComment.getContent();
//        commentService.updateComment(postId, commentId, email, content);
//        return new ApiResponse<>("Comment updated successfully");
//
//    }
//
//    // 포스트의 model 다운로드
//    @GetMapping("/models")
//    public ResponseEntity<Resource> downloadModel(@RequestParam("modelName") String modelName,
//                                                  @RequestHeader(required = false, name = HttpHeaders.AUTHORIZATION) String token) {
//        PostDTO.PostInfo post = postService.getPost(modelName);
//        Long userId = token == null ? null : userAuthService.getUserId(token);
//        if (((post.getStatus() == PostEntity.PostStatus.PRIVATE) && !(post.getUserId().equals(userId)))) {
//            throw new UnAuthorizedException("UnAuthorized Exception");
//        }
//        Resource resource = fileService.getFileResource(modelName, FileService.Extension.GLB);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "model.glb")
//                .body(resource);
//    }
//
//    //ai이용 포스트 생성
//    @PostMapping("/ai")
//    public ApiResponse<PostDTO.AiResponse> aiService(
//            @RequestParam(defaultValue = "false") Boolean autoCreate,
//            @RequestParam(defaultValue = "PUBLIC") String status,
//            PostDTO.files files,
//            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
//    ) throws Exception {
//        MultipartFile filePng = files.getFile();
//        MultipartFile fileGlb = files.getFileGlb();
//        List<String> response = aiService.getDescription(filePng);
//        PostDTO.AiResponse aiResponse = new PostDTO.AiResponse(response.get(0), response.get(1), List.of(response.get(2).split(",")));
//        if (autoCreate) {
//            Long userId = userAuthService.getUserId(token);
//            postService.createPost(
//                    PostDTO.PostCreateDTO.builder()
//                            .title(aiResponse.getTitle())
//                            .content(aiResponse.getDescription())
//                            .tags(aiResponse.getTags())
//                            .file(fileGlb)
//                            .status(PostEntity.PostStatus.valueOf(status))
//                            .build()
//                    , userId
//            );
//            return new ApiResponse<>("GEMINI API CREATED REPOSITORY SUCCESSFUL");
//        } else {
//            return new ApiResponse<>("Gemini API response successful.", aiResponse);
//        }
//
//    }


}
