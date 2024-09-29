package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.adapter.in.web.dto.UserDTO.LoginDTO;
import com.kjs990114.goodong.application.port.in.auth.LoginUseCase;
import com.kjs990114.goodong.application.port.in.auth.LoginUseCase.LoginCommand;
import com.kjs990114.goodong.application.port.in.auth.RegisterUseCase;
import com.kjs990114.goodong.application.service.UserAuthService;
import com.kjs990114.goodong.application.service.UserService;
import com.kjs990114.goodong.common.exception.UnAuthorizedException;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.adapter.in.web.dto.ApiResponse;
import com.kjs990114.goodong.adapter.in.web.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static com.kjs990114.goodong.application.port.in.auth.RegisterUseCase.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthEndpoint {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginDTO loginDTO) {
        LoginCommand loginCommand = new LoginCommand(loginDTO.getEmail(),loginDTO.getPassword());
        String token = loginUseCase.login(loginCommand);
        return new ApiResponse<>("Login Success", token);
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody UserDTO.Register registerDTO) {
        RegisterCommand registerCommand = new RegisterCommand(registerDTO.getEmail(),registerDTO.getNickname(),registerDTO.getPassword());
        registerUseCase.register(registerCommand);
        return new ApiResponse<>("Register Success");
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public ApiResponse<Void> deleteUserAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (!userAuthService.getUserId(token).equals(userId)) {
            throw new UnAuthorizedException("User authorization failed");
        }
        userService.deleteUser(userId);
        return new ApiResponse<>("User account deleted successfully");
    }

    @GetMapping("/register/check-nickname")
    public ApiResponse<Boolean> checkNickname(@RequestParam(name = "nickname") String nickname) {
        return new ApiResponse<>(registerUseCase.isDuplicateNickname(nickname));
    }

    @GetMapping("/register/check-email")
    public ApiResponse<Boolean> checkEmail(@RequestParam(name = "email") String email) {
        return new ApiResponse<>(registerUseCase.isDuplicateEmail(email));
    }

    @PostMapping("/register/check-password")
    public ApiResponse<Boolean> checkPassword(@RequestBody UserDTO.Password password) {
        return new ApiResponse<>(registerUseCase.isValidPassword(password.getPassword()));
    }

    @GetMapping("/check-token")
    public ApiResponse<UserDTO.UserSummary> checkToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return new ApiResponse<>("Token validation successful",.getUserInfoSummary(userId));
    }

    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@RequestParam("userId") Long userId,
                                            @RequestBody UserDTO.Password password,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (!userAuthService.getUserId(token).equals(userId)) {
            throw new UnAuthorizedException("User Authorization failed");
        }
        userAuthService.changePassword(userId, password.getPassword());
        return new ApiResponse<>("Password change success");
    }


}