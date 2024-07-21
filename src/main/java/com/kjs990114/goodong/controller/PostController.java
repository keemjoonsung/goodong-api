package com.kjs990114.goodong.controller;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.kjs990114.goodong.document.PostDocument;
import com.kjs990114.goodong.dto.PostDTO;
import com.kjs990114.goodong.entity.PostEntity;
import com.kjs990114.goodong.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@RestController
@RequiredArgsConstructor
@RequestMapping("/repository")
public class PostController {

    private final PostService postService;
    @Value("${model.location}")
    String location;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private final String bucketName;


    private final Storage storage;
    @PostMapping("/savepost")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> savePost(@RequestParam("file") MultipartFile file,
                                           @RequestParam("title") String title,
                                           @RequestParam("content") String content,
                                           @RequestParam("userId") String userId,
                                           @RequestParam("uploadDate") String uploadDate)

    {
        try {
            System.out.println("title = " + title);
            System.out.println("content = " + content);
            System.out.println("userId = " + userId);
            System.out.println("uploadDate = " + uploadDate);
            System.out.println("file = " + file);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date parsedDate = dateFormat.parse(uploadDate);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + ".glb";

            PostDTO postDTO = new PostDTO(title, content, userId, timestamp ,fileName);

            storage.create(
                    BlobInfo.newBuilder(bucketName, fileName).build(),
                    file.getBytes()
            );

            postService.savePost(postDTO);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("failed");
        }
    }


    @GetMapping("/showpost")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<PostEntity>> showPostAll( @RequestParam("username") String username) {
        List<PostEntity> post = postService.getPostByUserId(username);
        return ResponseEntity.ok(post);
    }


    @GetMapping("/searchPosts")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<PostDocument>> searchPostAll( @RequestParam("keyword") String keyword) {
        List<PostDocument> post = postService.searchPosts(keyword);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/showpostByPostId")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<PostEntity> showPostByPostId(@RequestParam("postId") String postId){
        Long id = Long.parseLong(postId);
        System.out.println("id = " + id);
        return ResponseEntity.ok(postService.getPostByPostId(id));

    }

    @GetMapping("/download/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Resource> downloadModelFile(@PathVariable("id") Long id)  {
        String fileName = postService.getPostByPostId(id).getFileUrl();
        System.out.println(fileName);
        Blob blob = storage.get(bucketName, fileName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blob.downloadTo(outputStream);

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(resource);

    }


}
