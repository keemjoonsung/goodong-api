package com.kjs990114.goodong.application.service.auth;

import com.kjs990114.goodong.application.port.in.auth.LoginUseCase;
import com.kjs990114.goodong.common.exception.Error;
import com.kjs990114.goodong.common.exception.ErrorException;
import com.kjs990114.goodong.common.jwt.JwtUtil;
import com.kjs990114.goodong.common.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    @Override
    public String login(LoginCommand loginCommand) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginCommand.getEmail(), loginCommand.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = Long.parseLong(customUserDetails.getUsername());
            return jwtUtil.createJwt(userId);
        }catch (Exception e){
            throw new ErrorException(Error.LOGIN_FAILED);
        }
    }
}