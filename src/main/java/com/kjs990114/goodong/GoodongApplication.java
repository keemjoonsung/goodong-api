package com.kjs990114.goodong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class GoodongApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodongApplication.class, args);
    }


}