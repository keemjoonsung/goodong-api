package com.kjs990114.goodong.application.dto;

import lombok.Data;

@Data
public class ModelInfoDTO {
    private int version;
    private String url;
    private String commitMessage;

    public ModelInfoDTO(int version, String url, String commitMessage){
        this.version = version;
        this.url = url;
        this.commitMessage = commitMessage;
    }
}
