package com.kjs990114.goodong.application.port.out.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StoreFilePort {
    String storeFile(MultipartFile file) throws IOException;
}
