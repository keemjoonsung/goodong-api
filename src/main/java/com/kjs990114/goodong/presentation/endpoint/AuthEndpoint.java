package com.kjs990114.goodong.presentation.endpoint;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthEndpoint {

    private final UserAuthService userAuthService;

    @PostMapping("/login")
    public CommonResponseEntity<String> login(@RequestBody UserDTO.Login login) {
        return new CommonResponseEntity<>("Login Success",userAuthService.login(login));
    }

    @PostMapping("/register")
    public CommonResponseEntity<Void> register(@RequestBody UserDTO.Register register) {
        userAuthService.register(register);
        return new CommonResponseEntity<>("Register Success");
    }

    @GetMapping
    public CommonResponseEntity<UserDTO.Summary> getAuth(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return new CommonResponseEntity<>("Token validation successful",userAuthService.getUserInfo(token));
    }
}