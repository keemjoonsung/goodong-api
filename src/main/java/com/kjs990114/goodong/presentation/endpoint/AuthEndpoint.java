package com.kjs990114.goodong.presentation.endpoint;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.presentation.dto.CommonResponseEntity;
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
    public CommonResponseEntity<String> login(@RequestBody UserDTO.LoginRequest loginRequest) {
        return new CommonResponseEntity<>("Login Success",userAuthService.login(loginRequest).getJwt());
    }

    @PostMapping("/register")
    public CommonResponseEntity<Void> register(@RequestBody UserDTO.RegisterRequest registerRequest) {
        userAuthService.register(registerRequest);
        return new CommonResponseEntity<>("Register Success");
    }

    @GetMapping
    public CommonResponseEntity<UserDTO.UserInfo> getAuth(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return new CommonResponseEntity<>("token validation successful",userAuthService.getUserInfo(token));
    }
}