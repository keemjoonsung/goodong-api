package com.kjs990114.goodong.application.service.post;

import com.kjs990114.goodong.application.port.in.file.GetFileResourceUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadPostPort;
import com.kjs990114.goodong.application.port.out.storage.LoadFilePort;
import com.kjs990114.goodong.common.exception.Error;
import com.kjs990114.goodong.common.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class LoadFileService implements GetFileResourceUseCase {

    private final LoadPostPort loadPostPort;
    private final LoadFilePort loadFilePort;

    @Transactional
    @Override
    public Resource getFileResource(LoadFileResourceQuery loadFileResourceQuery) {
        Long userId = loadFileResourceQuery.getUserId();
        String fileName = loadFileResourceQuery.getFileName();
        if(!loadPostPort.isAccessibleByUserId(userId,fileName)){
            throw new ErrorException(Error.UNAUTHORIZED_ACCESS);
        }
        InputStream inputStream = loadFilePort.loadFile(fileName);
        return new InputStreamResource(inputStream);
    }
}
