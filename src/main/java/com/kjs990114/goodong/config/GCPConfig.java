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
//    private static final String credentialsJson =  "{" +"\"type\": \"service_account\",\n" +
//        "  \"project_id\": \"goodong\",\n" +
//        "  \"private_key_id\": \"7ab19e6ca5d2be40ed78db83ad7721ac075d5a89\",\n" +
//        "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCgrm8uLYHBbw3D\\nC9MvJKJFrcDaEFJBbfe05zf7o61ibojI7NbRxD82EW2SDyYLPGBeQMtOx+7QVDdm\\nV0yJGeEPlgSWkpEV+jmmOL3+InRmL/rhGVudaCAOnySC83l1vi5AhD1Uoh77+D+K\\nnIUvGkhyC3wZSMjrDPjP7lxCTOu9YZvcYjs5xwzzgtN4ekOV0SuDDv5G1b613IoM\\nRhA0CHmidregh7OeR0Ja/fzugCRZcVeAuLcgTE35gZtpmEbUC9kzxmQMxyHaiff/\\n0uM9goJpbv3JzLakgJXasGB4TSZdjkMmwdNKM4M3xLF+l2ZkP2YVTWuU1EhBwV6Q\\nwJxLQTlNAgMBAAECggEAInwVWe6D0tEj/biPuCYvWm92PIa0tJymhTF7gxrndwDc\\ng+zKlOX+n/rBDpAIPPCWCRx0VcHUh+sPIbx6LVF7yyXJ8cvebf5QOZOjeQOJgjXp\\nbL6NujxS5vhyNFWPO30XvZpz9DJAHpkyf+vkLI4CPtGmrmF/12xnCO8x5zvNe0z8\\ngMbxfLSjCSslQU4G2EARHECoVEccp522eyL/ObeSIP93PD//Lal6CROfEFGJbDDJ\\ns1q8EBPiDKgKnJWzwTuQfjzlbiwsa8+6NFajeb76rwSWge3nTY64eNPWDOxEZyKm\\n+J7RybIa6z5a/Kq3B73R8MMyGmUEsfRHy4CHLvhQIQKBgQDa1jQ1TDFIYR7kzGP6\\n80eqwcVyQRpOoJZJeFFPF3XAnQeN2KzORzALjUrnnpjiCI81W9YvWjQslF12MDgw\\nwjM1Q9ntb5AJDV/KOnrzIRsbUOyGup69jgW78XKMUPendJOg33UH1GLr5iHaKvIl\\nQq1nV3/ZAHQRmBJbIW+Wwp1v7QKBgQC79/YYm8rkUCwX6D/eul9vI83GYCe+UMHg\\nHbI7jLQRzvY5Ij2xCqc6wuBS0+F1KkMDnTLOj3qby5n2xe418yKzQcCYnLQmHsUw\\n3k3od/uErBdTOdppvi6yu5oLtQbRBUV0r1S5EWS0CJYeeme6L56a4E81+MrefA3x\\n19RGH8oC4QKBgCTWWgN+x+z/LEwne80Kls3PwITzIJKLxcCoWEeQE2pRBKyEBNLU\\neOn45tsGf4XNGw5IX02pSY8XioQSqeLLTgNxYIXvebeMYMuR/JBgykdW3/nuADq4\\nZY61niqwln/Bx/gUpYaj4eMD4j7va8OJ7davi/3jbaZoiu58OmSObiIhAoGBALW3\\nUM2XhzzTmNz/5Qcwi+RI67z05lchgVxluRHCw65Xs0emp5dJIkQ3c1//f/2h/qk2\\nxpBUuwZ0fYLI9LJpO8mqLkQbcjlME/xbXVv46knniKEn09PYTRLzNEhPGKjnfK8C\\ndVh4zS8TAfWzBiZDtQQrB8i4nX8a1OxRZ+DnCIdhAoGAN3tEtGQvA+3o/grdjXcm\\nykYeifz1STI5uLB8qPoxwkvo7TZ+ViCmdgpyCvhkcWSE82E5h0CmORM0z1ppTmYK\\nSpUBQob/FJylew8jQvG/e1CLqz5dnVI8kbEMGPrkMvyjwTI16GNYffs8HAdxZoO7\\nHJxZRbHoAZj/UrWw5anwZpk=\\n-----END PRIVATE KEY-----\\n\",\n" +
//        "  \"client_email\": \"741693435028-compute@developer.gserviceaccount.com\",\n" +
//        "  \"client_id\": \"107471245941404549967\",\n" +
//        "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
//        "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
//        "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
//        "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/741693435028-compute%40developer.gserviceaccount.com\",\n" +
//        "  \"universe_domain\": \"googleapis.com\"\n" +
//        "}";


    @Bean
    public Storage storage() throws IOException {
        System.out.println("크레덴셜 : " + credentialsJson);
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsJson.getBytes()))
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}