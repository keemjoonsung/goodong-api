package com.kjs990114.goodong.common.jwt.filter;

import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.common.userdetails.CustomUserDetails;
import com.kjs990114.goodong.domain.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class jwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public jwtFilter(JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {

        String token= request.getHeader("Authorization");
        System.out.println("authorization = " + token);
        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("authorization now");

        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return;
        }

        String nickname = jwtUtil.getNickname(token);
        String role = jwtUtil.getRole(token);
        String email = jwtUtil.getEmail(token);
        System.out.println("nickname = " + nickname);
        System.out.println("role = " + role);
        System.out.println("email = " + email);
        User user = User.builder()
                .nickname(nickname)
                .email(email)
                .role(User.Role.valueOf(role))
                .password("")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}