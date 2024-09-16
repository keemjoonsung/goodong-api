package com.kjs990114.goodong.application.file;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.domain.post.Model;
import com.kjs990114.goodong.domain.post.Post;
import com.kjs990114.goodong.domain.post.repository.ModelRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final ModelRepository modelRepository;
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    private final Storage storage;

    public Resource getFileResource(String fileName, Extension extension) {
        Blob blob = storage.get(bucketName, fileName + extension.getExtension());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blob.downloadTo(outputStream);

        return new ByteArrayResource(outputStream.toByteArray());
    }

    public String saveFileStorage(MultipartFile file, Extension extension) throws IOException {
        String uuid = UUID.randomUUID().toString();
        storage.create(
                BlobInfo.newBuilder(bucketName, uuid + extension.getExtension()).build(),
                file.getBytes()
        );

        return uuid;
    }
    public Post getPost(String fileName){
        Model model =  modelRepository.findByFileName(fileName).orElseThrow(()->new GlobalException("Model does not exist"));
        return model.getPost();
    }
    @Getter
    public enum Extension {
        GLB(".glb"),
        PNG(".png");
        private final String extension;
        Extension(String extension){
            this.extension = extension;
        }

    }

}

