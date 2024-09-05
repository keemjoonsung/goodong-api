package com.kjs990114.goodong.presentation.endpoint;

import com.kjs990114.goodong.application.post.PostService;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostEndpoint {

    private final PostService postService;

    //포스트 생성
    @PostMapping
    public CommonResponseEntity<Void> createPost(@RequestBody PostDTO.Create create,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        postService.createPost(create, token);
        return new CommonResponseEntity<>("Post created successfully");
    }

    // 유저의 post 리스트 반환
    @GetMapping
    public CommonResponseEntity<List<PostDTO.Summary>> getUserPosts(@RequestParam("email") String email,
                                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        return new CommonResponseEntity<>(postService.getUserPosts(email, token));
    }

    //검색 elastic search
    @GetMapping("/search")
    public CommonResponseEntity<List<PostDTO.Summary>> searchPosts(@RequestParam("keyword") String keyword) {
        return new CommonResponseEntity<>(postService.searchPosts(keyword));
    }

    //
//    // 게시글 Update
//    @PutMapping("/{postId}")
//    public ResponseEntity<String> updatePost(@PathVariable("postId") String postId, @RequestBody PostDTO postDTO) {
//        return ResponseEntity.ok("success");
//    }
//
//    // 게시글 Delete
//    @DeleteMapping("/{postId}")
//    public ResponseEntity<String> deletePost(@PathVariable("postId") String postId) {
//        return ResponseEntity.ok("success");
//    }
//
    // 해당 포스트 정보만 가져오기
    @GetMapping("/{postId}")
    public CommonResponseEntity<PostDTO.Detail> getPost(@PathVariable("postId") Long postId) {
        return new CommonResponseEntity<>(postService.getPost(postId));
    }
//
//    //댓글달기
//    @PostMapping("/{postId}/comments")  // 댓글 달
//    public ResponseEntity<String> addComment(@PathVariable("postId") Long postId,
//                                             @RequestParam("userId") Long userId,
//                                             @RequestParam("comment") String comment) {
//        return ResponseEntity.ok("Comment added successfully");
//    }
//
//    // 좋아요 달기
//    @PostMapping("/{postId}/likes")
//    public ResponseEntity<String> addLike(@PathVariable("postId") Long postId,
//                                          @RequestParam("userId") Long userId) {
//        return ResponseEntity.ok("Like added successfully");
//    }
//
//    //해당 포스트 다운로드 (glb로)
//    @GetMapping("/download/{postId}")
//    public ResponseEntity<Resource> downloadModel(@PathVariable("postId") Long postId) {
//        String fileName = postService.getPostByPostId(postId).getFileUrl();
//        System.out.println(fileName);
//        Blob blob = storage.get(bucketName, fileName);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        blob.downloadTo(outputStream);
//
//        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
//                .body(resource);
//
//    }


}
