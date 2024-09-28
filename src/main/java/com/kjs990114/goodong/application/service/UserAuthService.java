package com.kjs990114.goodong.application.service;

import com.kjs990114.goodong.adapter.out.persistence.entity.UserEntity;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.common.userdetails.CustomUserDetails;
import com.kjs990114.goodong.common.jwt.util.JwtUtil;
import com.kjs990114.goodong.adapter.out.persistence.repository.UserRepository;
import com.kjs990114.goodong.adapter.in.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$";



    @Transactional
    public void register(UserDTO.Register register) {

        UserEntity userEntity = UserEntity.builder()
                .email(register.getEmail())
                .password(passwordEncoder.encode(register.getPassword()))
                .nickname(register.getNickname())
                .build();

        userRepository.save(userEntity);
    }


    @Transactional
    public void changePassword(Long userId, String newPassword) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        userEntity.changePassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    public Long getUserId(String token){
        System.out.println(token);
        ValueOperations<String,Object> valueOp = redisTemplate.opsForValue();
        String key = "userToken:"  + token;
        Object cachedData = valueOp.get(key);
        if(cachedData != null) {
            return Long.parseLong(cachedData.toString());
        }
        UserEntity userEntity = userRepository.findByEmail(jwtUtil.getEmail(token)).orElseThrow(() -> new NotFoundException("User does not exists"));
        Long dbData = userEntity.getUserId();
        valueOp.set(key,dbData);
        return dbData;
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
