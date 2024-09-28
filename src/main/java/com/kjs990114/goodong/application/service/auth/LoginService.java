package com.kjs990114.goodong.application.service.auth;

import com.kjs990114.goodong.application.port.in.auth.LoginUseCase;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.common.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    @Override
    public String login(LoginCommand loginCommand) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginCommand.getEmail(), loginCommand.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String nickname = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        System.out.println("로그인성공");
        return jwtUtil.createJwt(loginCommand.getEmail(), nickname, role, 60 * 60 * 60 * 60 * 60 * 10L);
    }
}
