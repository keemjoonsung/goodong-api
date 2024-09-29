package com.kjs990114.goodong.adapter.out.persistence.gcp;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.kjs990114.goodong.application.port.out.gcp.StoreFilePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FilePersistenceAdapter implements StoreFilePort {
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    private final Storage storage;

    @Override
    public String storeGlbFile(MultipartFile glbFile) throws IOException {
        if(glbFile.isEmpty()) return "";
        String uuid = UUID.randomUUID().toString();
        storage.create(
                BlobInfo.newBuilder(bucketName, uuid + ".glb").build(),
                glbFile.getBytes()
        );
        return uuid;

    }
}
