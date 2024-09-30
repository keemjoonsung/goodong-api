package com.kjs990114.goodong.application.port.out.storage;

import java.io.InputStream;

public interface LoadFilePort {
    InputStream loadFile(String fileName);
}
