package com.kjs990114.goodong.config;

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
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsJson.getBytes()))
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}