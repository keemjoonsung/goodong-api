package com.kjs990114.goodong.application.dto;

import lombok.Data;

@Data
public class ModelInfoDTO {
    private int version;
    private String url;
    private String fileName;
    private String commitMessage;

    public ModelInfoDTO(int version, String fileName, String commitMessage){
        this.version = version;
        this.url = fileName;
        this.fileName = fileName;
        this.commitMessage = commitMessage;
    }
}
