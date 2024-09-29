package com.kjs990114.goodong.application.port.out.gcp;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StoreFilePort {
    String storeGlbFile(MultipartFile glbFile) throws IOException;
}
