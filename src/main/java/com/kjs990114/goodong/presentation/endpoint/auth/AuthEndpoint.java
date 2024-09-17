package com.kjs990114.goodong.presentation.endpoint.auth;

import com.kjs990114.goodong.application.auth.UserAuthService;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import jakarta.validation.Valid;
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
    public CommonResponseEntity<Void> register(@Valid @RequestBody UserDTO.Register register) {
        userAuthService.register(register);
        return new CommonResponseEntity<>("Register Success");
    }

    @GetMapping
    public CommonResponseEntity<UserDTO.UserSummary> getAuth(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return new CommonResponseEntity<>("Token validation successful",userAuthService.getUserInfo(token));
    }

    @PutMapping("/password")
    public CommonResponseEntity<Void> changePassword(@RequestParam("userId") Long userId,
                                                     @RequestBody UserDTO.Password password,
                                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(!userAuthService.getUserInfo(token).getUserId().equals(userId)){
            throw new UnAuthorizedException("User Authorization failed");
        }
        userAuthService.changePassword(userId,password.getPassword());
        return new CommonResponseEntity<>("Password change success");
    }

    @GetMapping("/duplicated")
    public CommonResponseEntity<Boolean> duplicate(@RequestParam(required = false, name="email") String email,
                                                     @RequestParam(required = false, name="nickname") String nickname){
        if(email == null && nickname == null) {
            throw new NotFoundException("Email and Nickname cannot be null");
        }
        if(email != null && nickname != null) {
            throw new NotFoundException("Choose one query parameter, either 'email' or 'nickname'.");
        }
        if (email != null) {
            return new CommonResponseEntity<>(userAuthService.isEmailDuplicated(email));
        }

        return new CommonResponseEntity<>(userAuthService.isNicknameDuplicated(nickname));
    }


    @PostMapping("/valid")
    public CommonResponseEntity<Boolean> validPassword(@RequestBody UserDTO.Password password){
        return new CommonResponseEntity<>(userAuthService.isPasswordValid(password.getPassword()));
    }
}