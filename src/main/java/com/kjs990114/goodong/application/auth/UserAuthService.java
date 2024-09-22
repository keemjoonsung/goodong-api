package com.kjs990114.goodong.application.auth;

import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.common.userdetails.CustomUserDetails;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.domain.user.repository.UserRepository;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$";

    @Transactional(readOnly = true)
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
        return jwtUtil.createJwt(login.getEmail(), nickname, role, 60 * 60 * 60 * 60 * 60 * 10L);
    }

    @Transactional
    public void register(UserDTO.Register register) {

        User user = User.builder()
                .email(register.getEmail())
                .password(passwordEncoder.encode(register.getPassword()))
                .nickname(register.getNickname())
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserDTO.UserSummary getUserInfo(String token) {
        User user = userRepository.findByEmail(jwtUtil.getEmail(token)).orElseThrow(() -> new NotFoundException("User session Expired!"));
        return new UserDTO.UserSummary(user.getUserId(), user.getEmail(), user.getNickname(), user.getProfileImage());
    }

    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        user.changePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isEmailDuplicated(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    @Transactional(readOnly = true)
    public boolean isNicknameDuplicated(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }
    @Transactional(readOnly = true)
    public boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


}
