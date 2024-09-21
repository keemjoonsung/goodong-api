package com.kjs990114.goodong.application.file;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
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

