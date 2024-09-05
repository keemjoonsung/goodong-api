package com.kjs990114.goodong.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class GCPConfig {

    @Value("${google.cloud.credentials}")
    private String credentialsJson;


    @Bean
    public Storage storage() throws IOException {
        System.out.println("크레덴셜ㄴ : " + credentialsJson);
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsJson.getBytes()))
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}