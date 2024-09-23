package com.kjs990114.goodong.presentation.endpoint.post;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.application.post.CommentService;
import com.kjs990114.goodong.application.post.LikeService;
import com.kjs990114.goodong.application.post.PostService;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.PostDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

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
    public CommonResponseEntity<Void> createPost(PostDTO.Create create,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long userId = userAuthService.getUserInfo(token).getUserId();
        postService.createPost(create, userId);
        return new CommonResponseEntity<>("Post created successfully");
    }

    //내 포스트
    @GetMapping
    public CommonResponseEntity<Page<PostDTO.Summary>> getMyPosts(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
                                                                  @RequestParam(name = "page", defaultValue = "0") int page) {
        Long userId = userAuthService.getUserInfo(token).getUserId();
        Page<PostDTO.Summary> response = postService.getPosts(userId, userId, page);
        return new CommonResponseEntity<>(response);
    }

    // 유저의 posts
    @GetMapping(params = "userId")
    public CommonResponseEntity<Page<PostDTO.Summary>> getUserPosts(@RequestParam(name = "userId") Long userId,
                                                                    @RequestHeader(required = false, name = HttpHeaders.AUTHORIZATION) String token,
                                                                    @RequestParam(name = "page", defaultValue = "0") int page) {
        Long viewerId = token == null ? null : userAuthService.getUserInfo(token).getUserId();
        Page<PostDTO.Summary> response = postService.getPosts(userId, viewerId, page);
        return new CommonResponseEntity<>(response);
    }

    // posts 검색
    @GetMapping(params = "query")
    public CommonResponseEntity<Page<PostDTO.Summary>> searchPosts(@RequestParam("query") String keyword,
                                                                   @RequestParam(name = "page", defaultValue = "0") int page) {
        return new CommonResponseEntity<>(postService.searchPosts(keyword, page));
    }

    @GetMapping("/all")
    public CommonResponseEntity<List<PostDTO.Summary>> getUserPosts(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long viewerId = userAuthService.getUserInfo(token).getUserId();
        List<PostDTO.Summary> response = postService.getMyPosts(viewerId);
        return new CommonResponseEntity<>(response);
    }

    // 게시글 Update
    @PatchMapping("/{postId}")
    public CommonResponseEntity<Void> updatePost(@PathVariable("postId") Long postId,
                                                 PostDTO.Update postDTO,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        Long userId = userAuthService.getUserInfo(token).getUserId();
        if (!userId.equals(postService.getPost(postId).getUserId())) {
            throw new UnAuthorizedException("UnAuthorized Exception");
        }
        postService.updatePost(postId, postDTO);
        return new CommonResponseEntity<>("Update success");
    }

    // 게시글 Delete
    @DeleteMapping("/{postId}")
    public CommonResponseEntity<Void> deletePost(@PathVariable("postId") Long postId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = userAuthService.getUserInfo(token).getUserId();
        if (!userId.equals(postService.getPost(postId).getUserId())) {
            throw new UnAuthorizedException("UnAuthorized Exception");
        }
        postService.deletePost(postId);
        return new CommonResponseEntity<>("Delete success");
    }

    // 특정 게시글 정보 가져오기
    @GetMapping("/{postId}")
    public CommonResponseEntity<PostDTO.PostDetail> getPost(@PathVariable("postId") Long postId,
                                                            @RequestHeader(required = false, value = HttpHeaders.AUTHORIZATION) String token
    ) {
        Long viewerId = token == null ? null : userAuthService.getUserInfo(token).getUserId();
        if (!postService.getPost(postId).getUserId().equals(viewerId) && postService.getPost(postId).getStatus().equals(Post.PostStatus.PRIVATE)) {
            throw new UnAuthorizedException("UnAuthorized Exception");
        }
        PostDTO.PostDetail postDetail = postService.getPost(postId);
        if (viewerId != null) {
            postDetail.setLiked(likeService.isLiked(postId, viewerId));
        }
        postDetail.setLikes(likeService.getLikesCount(postId));
        postDetail.setComments(commentService.getComments(postId));
        return new CommonResponseEntity<>(postDetail);
    }

    //중복체크
    @GetMapping("/check-title")
    public CommonResponseEntity<Boolean> isDuplicateTitle(@RequestParam("title") String title,
                                                          @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = userAuthService.getUserInfo(token).getUserId();
        return new CommonResponseEntity<>(postService.checkDuplicatedTitle(title, userId));
    }


}
