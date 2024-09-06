package com.kjs990114.goodong.application.auth;

import com.kjs990114.goodong.common.exception.GlobalException;
import com.kjs990114.goodong.common.userdetails.CustomUserDetails;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public String login(UserDTO.Login login) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String nickname = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        return jwtUtil.createJwt(login.getEmail(),nickname, role,60*60*60*60*60*10L);
    }

    public void register(UserDTO.Register register) {

        User user = User.builder()
                .email(register.getEmail())
                .password(passwordEncoder.encode(register.getPassword()))
                .nickname(register.getNickname())
                .build();

        userRepository.save(user);
    }

    public UserDTO.UserSummary getUserInfo(String token) {
        User user =userRepository.findByEmail(jwtUtil.getEmail(token)).orElseThrow(()-> new GlobalException("User does not exists"));
        return new UserDTO.UserSummary(user.getUserId(),user.getEmail(),user.getNickname(),user.getProfileImage());
    }

    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(()-> new GlobalException("User does not exists"));
        user.changePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


}
