package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.application.dto.ApiResponse;
import com.kjs990114.goodong.application.dto.PostDTO;
import com.kjs990114.goodong.application.dto.PostDTO.*;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase.TokenQuery;
import com.kjs990114.goodong.application.port.in.file.GetFileResourceUseCase;
import com.kjs990114.goodong.application.port.in.post.*;
import com.kjs990114.goodong.application.port.in.post.CreatePostUseCase.CreatePostCommand;
import com.kjs990114.goodong.application.port.in.post.DeletePostUseCase.DeletePostCommand;
import com.kjs990114.goodong.application.port.in.post.GeneratePostMetadataUseCase.GetPostMetadataQuery;
import com.kjs990114.goodong.application.port.in.file.GetFileResourceUseCase.LoadFileResourceQuery;
import com.kjs990114.goodong.application.port.in.post.GetPostDetailUseCase.LoadPostDetailCommand;
import com.kjs990114.goodong.application.port.in.post.GetPostsByPageUseCase.LoadPostsByPageCommand;
import com.kjs990114.goodong.application.port.in.post.SearchPostsByPageUseCase.SearchPostsByPageQuery;
import com.kjs990114.goodong.application.port.in.post.UpdatePostUseCase.UpdatePostCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final CheckTokenUseCase checkTokenUseCase;
    private final CreatePostUseCase createPostUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    private final DeletePostUseCase deletePostUseCase;
    private final GetPostDetailUseCase getPostDetailUseCase;
    private final GetPostsByPageUseCase getPostsByPageUseCase;
    private final SearchPostsByPageUseCase searchPostsByPageUseCase;
    private final GeneratePostMetadataUseCase generatePostMetadataUseCase;
    private final GetFileResourceUseCase getFileResourceUseCase;

    @Value("${spring.page.size}")
    private int pageSize;

    //게시글 Create
    @PostMapping
    public ApiResponse<Void> createPost(PostCreateDTO postCreateDTO,
                                        @RequestParam(name = "autoCreate", defaultValue = "false") boolean autoCreate,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        String title = postCreateDTO.getTitle();
        String content = postCreateDTO.getContent();
        List<String> tags = postCreateDTO.getTags();
        if (autoCreate) {
            PostMetadataDTO postMetadata = generatePostMetadataUseCase.getPostMetadata(new GetPostMetadataQuery(postCreateDTO.getFilePng()));
            System.out.println("postMetadata = " + postMetadata);
            title = postMetadata.getTitle();
            content = postMetadata.getContent();
            tags = postMetadata.getTags();
        }
        System.out.println("content : " + content);
        CreatePostCommand createPostCommand = CreatePostCommand.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .tags(tags)
                .file(postCreateDTO.getFileGlb())
                .status(postCreateDTO.getStatus())
                .build();
        createPostUseCase.createPost(createPostCommand);
        return new ApiResponse<>("Post created successfully");
    }

    // 게시글 Update
    @PatchMapping("/{postId}")
    public ApiResponse<Void> updatePost(@PathVariable("postId") Long postId,
                                        PostUpdateDTO postUpdateDTO,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        UpdatePostCommand updatePostCommand = UpdatePostCommand.builder()
                .title(postUpdateDTO.getTitle())
                .content(postUpdateDTO.getContent())
                .commitMessage(postUpdateDTO.getCommitMessage())
                .status(postUpdateDTO.getStatus())
                .file(postUpdateDTO.getFileGlb())
                .tags(postUpdateDTO.getTags())
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
        if (query != null && !query.isEmpty() && !query.isBlank()) {
            Pageable pageable = allPage ? Pageable.unpaged() : PageRequest.of(page, pageSize);
            response = searchPostsByPageUseCase.searchPostsByPage(new SearchPostsByPageQuery(query, pageable));
        } else {
            Pageable pageable = allPage ? Pageable.unpaged(Sort.by("lastModifiedAt").descending()) : PageRequest.of(page, pageSize, Sort.by("lastModifiedAt").descending());
            Long viewerId = token == null ? null : checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
            Long ownerId = userId == null ? viewerId : userId;
            response = getPostsByPageUseCase.getPostByPage(new LoadPostsByPageCommand(ownerId, viewerId, pageable));
        }

        return new ApiResponse<>(response);
    }



    @PostMapping("/metadata")
    public ApiResponse<PostMetadataDTO> getMetadata(@RequestPart(name = "filePng") MultipartFile filePng) throws IOException {
        return new ApiResponse<>(generatePostMetadataUseCase.getPostMetadata(new GetPostMetadataQuery(filePng)));
    }

    // 포스트의 model 다운로드
    @GetMapping("/models")
    public ResponseEntity<Resource> downloadModel(@RequestParam("fileName") String fileName,
                                                                              @RequestHeader(required = false, name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = token == null ? null : checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        Resource resource = getFileResourceUseCase.getFileResource(new LoadFileResourceQuery(userId, fileName));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .body(resource);
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


}
