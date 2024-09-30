package com.kjs990114.goodong.application.port.in.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

public interface GetFileResourceUseCase {

    Resource getFileResource(LoadFileResourceQuery loadFileResourceQuery);

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class LoadFileResourceQuery {
        private Long userId;
        private String fileName;
    }

}
