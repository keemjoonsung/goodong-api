//package com.kjs990114.goodong.service;
//
//import com.kjs990114.goodong.presentation.dto.request.UserRegisterDTO;
//import com.kjs990114.goodong.entity.UserEntity;
//import com.kjs990114.goodong.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class RegisterUserService {
//    private final BCryptPasswordEncoder encoder;
//    private final UserRepository userRepository;
//    @Transactional
//    public void registerUser(UserRegisterDTO user) throws Exception{
//
//        if(userRepository.existsByUsername(user.getUsername())){
//            throw new DataIntegrityViolationException("Email already exists!");
//        }
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername(user.getUsername());
//        userEntity.setPassword(encoder.encode(user.getPassword()));
//        userEntity.setRole("ROLE_ADMIN");//일단 다 어드민
//        userRepository.save(userEntity);
//    }
//
//}
