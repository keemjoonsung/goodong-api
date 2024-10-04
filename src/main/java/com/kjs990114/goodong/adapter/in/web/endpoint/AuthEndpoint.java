package com.kjs990114.goodong.adapter.in.web.endpoint;

import com.kjs990114.goodong.application.dto.ApiResponse;
import com.kjs990114.goodong.application.dto.UserDTO.LoginDTO;
import com.kjs990114.goodong.application.dto.UserDTO.PasswordDTO;
import com.kjs990114.goodong.application.dto.UserDTO.UserRegisterDTO;
import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import com.kjs990114.goodong.application.port.in.auth.*;
import com.kjs990114.goodong.application.port.in.auth.ChangePasswordUseCase.PasswordQuery;
import com.kjs990114.goodong.application.port.in.auth.CheckTokenUseCase.TokenQuery;
import com.kjs990114.goodong.application.port.in.auth.LoginUseCase.LoginCommand;
import com.kjs990114.goodong.application.port.in.auth.WithDrawUseCase.WithDrawCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import static com.kjs990114.goodong.application.port.in.auth.RegisterUseCase.RegisterCommand;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthEndpoint {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final CheckTokenUseCase checkTokenUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final WithDrawUseCase withDrawUseCase;

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginDTO loginDTO) {
        LoginCommand loginCommand = new LoginCommand(loginDTO.getEmail(), loginDTO.getPassword());
        String token = loginUseCase.login(loginCommand);
        return new ApiResponse<>("Login Success", token);
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        RegisterCommand registerCommand = new RegisterCommand(userRegisterDTO.getEmail(), userRegisterDTO.getNickname(), userRegisterDTO.getPassword());
        registerUseCase.register(registerCommand);
        return new ApiResponse<>("Register Success");
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public ApiResponse<Void> deleteUserAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        withDrawUseCase.withdraw(new WithDrawCommand(userId));
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
    public ApiResponse<Boolean> checkPassword(@RequestBody PasswordDTO passwordDTO) {
        return new ApiResponse<>(registerUseCase.isValidPassword(passwordDTO.getPassword()));
    }

    @GetMapping("/check-token")
    public ApiResponse<UserSummaryDTO> checkToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return new ApiResponse<>("Token validation successful", checkTokenUseCase.checkToken(new TokenQuery(token)));
    }

    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@RequestBody PasswordDTO passwordDTO,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = checkTokenUseCase.checkToken(new TokenQuery(token)).getUserId();
        PasswordQuery passwordQuery = new PasswordQuery(userId, passwordDTO.getPassword());
        changePasswordUseCase.changePassword(passwordQuery);
        return new ApiResponse<>("Password change success");
    }


}