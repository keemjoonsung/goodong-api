package com.kjs990114.goodong.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class HomeController {
    @GetMapping("/api/hello")
    public String hello() {
        return "Test: " + new Date() + "\n";
    }
}