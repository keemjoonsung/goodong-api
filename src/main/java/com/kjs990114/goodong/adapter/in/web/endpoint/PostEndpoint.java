package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.application.dto.ApiResponse;

import com.kjs990114.goodong.application.dto.PostDTO.*;
import com.kjs990114.goodong.application.dto.PostSummaryDTO;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase.TokenQuery;
import com.kjs990114.goodong.application.port.in.file.GetFileResourceUseCase;
import com.kjs990114.goodong.application.port.in.post.*;
import com.kjs990114.goodong.application.port.in.post.CheckDuplicatePostTitleUseCase.CheckPostTitleQuery;
import com.kjs990114.goodong.application.port.in.post.CreatePostUseCase.CreatePostCommand;
import com.kjs990114.goodong.application.port.in.post.DeletePostUseCase.DeletePostCommand;
import com.kjs990114.goodong.application.port.in.post.GeneratePostMetadataUseCase.GetPostMetadataQuery;
import com.kjs990114.goodong.application.port.in.file.GetFileResourceUseCase.LoadFileResourceQuery;
import com.kjs990114.goodong.application.port.in.post.GetLikedPostsUseCase.LoadLikedPostsQuery;
import com.kjs990114.goodong.application.port.in.post.GetPostDetailUseCase.LoadPostDetailCommand;
import com.kjs990114.goodong.application.port.in.post.GetUserPostsUseCase.LoadPostsByPageCommand;
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
    private final GetUserPostsUseCase getUserPostsUseCase;
    private final GetLikedPostsUseCase getLikedPostsUseCase;
    private final SearchPostsByPageUseCase searchPostsByPageUseCase;
    private final GeneratePostMetadataUseCase generatePostMetadataUseCase;
    private final GetFileResourceUseCase getFileResourceUseCase;
    private final CheckDuplicatePostTitleUseCase checkDuplicatePostTitleUseCase;

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
            title = postMetadata.getTitle();
            content = postMetadata.getContent();
            tags = postMetadata.getTags();
        }
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
                                                          @RequestParam(name = "all", defaultValue = "false") boolean allPage,
                                                          @RequestParam(required = false, name = "likerId") Long likerId,
                                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestHeader(required = false, name = HttpHeaders.AUTHORIZATION) String token) {
        Page<PostSummaryDTO> response;
        if (query != null && !query.isBlank()) {
            Pageable pageable = allPage ? Pageable.unpaged() : PageRequest.of(page, pageSize);
            response = searchPostsByPageUseCase.searchPostsByPage(new SearchPostsByPageQuery(query, pageable));
        } else if(likerId == null) {
            Pageable pageable = allPage ? Pageable.unpaged(Sort.by("lastModifiedAt").descending()) : PageRequest.of(page, pageSize, Sort.by("lastModifiedAt").descending());
            Long viewerId = token == null ? null : checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
            Long ownerId = userId == null ? viewerId : userId;
            response = getUserPostsUseCase.getUserPosts(new LoadPostsByPageCommand(ownerId, viewerId, pageable));
        }else{
            Pageable pageable = allPage ? Pageable.unpaged(Sort.by("lastModifiedAt").descending()) : PageRequest.of(page, pageSize, Sort.by("lastModifiedAt").descending());
            Long viewerId = token == null ? null : checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
            response = getLikedPostsUseCase.getLikedPosts(new LoadLikedPostsQuery(likerId,viewerId,pageable));
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

    //중복체크
    @GetMapping("/check-title")
    public ApiResponse<Boolean> isDuplicateTitle(@RequestParam("title") String title,
                                                 @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        return new ApiResponse<>(checkDuplicatePostTitleUseCase.checkTitle(new CheckPostTitleQuery(title, userId)));
    }



}
