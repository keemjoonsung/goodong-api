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
//        private static final String credentialsJson = "{"
//        + "\"type\": \"service_account\","
//        + "\"project_id\": \"goodong-graduation-project\","
//        + "\"private_key_id\": \"bf6dd90095938bf8382842057da1c5b894d42d8e\","
//        + "\"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC5lFu0JQVOCo+N\\n/FuYxAcg+eUHIuBVJVtNOpVnFsH6NdFdV76F6HJP2iS74Yhy+0h7g9mlKdaUxE/G\\nEQVpK73Ke/Ryet45TFetfJSubao6y9PbxpVHiR8KRsxGmuwAjrBu8VHSis10YVy5\\nKwAkDBn+vdTvPWDRei0/TDkr//DqznM/eCnM+OoxumnAX9fG9YcuunMssbMqZsjE\\nkyXvt3qNEriz+H4jl9i4wNYg+n6PaOF8vIvErNKB2WWRilm707bM1HicZAWkkyNv\\nSXlUqO5C9PwUYJZY55dpyrPl34PklN2JspzE7zKRpjlasrgMenhe8DDcxMakH1Gi\\nghzVmxevAgMBAAECggEACVzAGEhxn3U8YVlU1W6MExP751Vttb+9cJntLmi6HxM2\\nxJgoBjMVfikIB0gJrWPFadnNLixuAaOsMtaxUz6VSvvgPrIO8P69er28vPLrR3Sj\\nV0SYS7RR2RF8hRlbLzPKwsKGQICK7mDcLhBnILGKC01eg6f4G56nsyoT4dxv0otR\\ndh7zsW856I1VMON3KjaSX+iBD9YuLoJ1Df3YNpX4eE63s6UyMXCc0dFTEwIY8z6x\\n7joYXfsFipsq3kK7FiBtPytrvylVVaiefeLIxUYWfNHtbawTJ3JSTdpKhijMYFBd\\nXPRfwOC+JZQhQJoaNYgqUALC7iXcOYoG+J3gwq3g8QKBgQDdtQ0DSgNv5HIAb3Pi\\nSBimhXvMvP2YmEoU36VtVRTUtFk1WIaCqNTYx+VoQZJ6a53/Mbpn571IxyTl1n9b\\nmGdRN8ObMhnHgezQaDsVWT7Zsu0J1q2QTir8sizT0AJQ42jfnbWgLDKlp571+G+a\\nOEuO8OWc76RZQ33JOGlrTHceEQKBgQDWSMHgSL0rV47QUmOsEQ887WHvHGkUGFYl\\nNeEWtfh24KnsaHuOqReF4EAf6kjc8FWYqzuUlrVFeY8EV06y1CciShxaMGfyCEsP\\nFod1l7KN/HaZHPbKj/RMjXmv49ibUEN5MX31My0uXuKdKmraGUE+i2TVeOE3frss\\nfcz/3gwZvwKBgAdpeSDmBY1lFsaa80XaYTCX/9aNbvD+DmP6Qh6QnJyr1tuotP6D\\nRHJm5G2C27HKDUMt12yH29UmCz6/2Awo4TNe6OQ0XZZE6rTg9zC0hhxjFfehPLY5\\nVsiR8Fu4dYtcvvHS5PKQ57SQEQlqH9kJDsWdmh+QB2+QiREEGMokiUrxAoGAD7bK\\nMO5koPcMzA4erRGbMnaeBfDpa8XVG2l2FjpF/EHBdEq9NNsFlJW7XhvKOBmPzWBR\\nQDZFGNEvQbw9UzPnosYYiiyekss05ehbCq6TM4JeGCBdoMEpwytkzxdAXTNnn02X\\n1RFpsMrYh3oC1/eeJz4FqPnyWiV7CGo5TFI2RPUCgYALBmxDjsDM0JWWf/4973tM\\nCDhBg63DJl+z9atBykwEycdE7ncM7Z6xfKeaaRMSvgUnMPG/kOgrt4N1hnsg0Xbe\\nfcYKQmvP7I2dYmrp8vgRmOIS0dHp0Yni1zno4VZUfp5JD3nAHdrjvYjc0zKvUj5r\\n7IhLAFjb4Wn8yegd/phO8A==\\n-----END PRIVATE KEY-----\\n\","
//        + "\"client_email\": \"383967023652-compute@developer.gserviceaccount.com\","
//        + "\"client_id\": \"102443516062425785049\","
//        + "\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\","
//        + "\"token_uri\": \"https://oauth2.googleapis.com/token\","
//        + "\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\","
//        + "\"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/383967023652-compute%40developer.gserviceaccount.com\","
//        + "\"universe_domain\": \"googleapis.com\""
//        + "}";
    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsJson.getBytes()))
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}