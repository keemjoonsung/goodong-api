package com.kjs990114.goodong.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProbeController {

    @GetMapping("/health")
    public ResponseEntity<String> probe(){
        return ResponseEntity.ok("ok24");
    }
}
