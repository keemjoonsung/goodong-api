package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.port.in.file.GetFileResourceUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadPostPort;
import com.kjs990114.goodong.application.port.out.storage.LoadFilePort;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class LoadFileService implements GetFileResourceUseCase {

    private final LoadPostPort loadPostPort;
    private final LoadFilePort loadFilePort;

    @Override
    public Resource getFileResource(LoadFileResourceQuery loadFileResourceQuery) {
        Long userId = loadFileResourceQuery.getUserId();
        String fileName = loadFileResourceQuery.getFileName();
        if(!loadPostPort.existsByUserIdAndFileName(userId,fileName)){
            throw new UnAuthorizedException("Unauthorized for file");
        }
        InputStream inputStream = loadFilePort.loadFile(fileName);
        return new InputStreamResource(inputStream);
    }
}
