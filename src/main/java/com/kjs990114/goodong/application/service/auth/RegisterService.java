package com.kjs990114.goodong.application.service.auth;

import com.kjs990114.goodong.application.port.in.auth.RegisterUseCase;
import com.kjs990114.goodong.application.port.out.CreateUserPort;
import com.kjs990114.goodong.application.port.out.LoadUserPort;
import com.kjs990114.goodong.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {

    private final CreateUserPort createUserPort;
    private final LoadUserPort loadUserPort;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void register(RegisterCommand registerCommand) {
        User user = User.builder()
                .email(registerCommand.getEmail())
                .nickname(registerCommand.getNickname())
                .build();
        user.changePassword(passwordEncoder.encode(registerCommand.getPassword()));
        createUserPort.save(user);
    }

    @Override
    public boolean isDuplicateNickname(String nickname) {
        return loadUserPort.existsByNickname(nickname);
    }

    @Override
    public boolean isDuplicateEmail(String email) {
        return loadUserPort.existsByEmail(email);
    }

    @Override
    public boolean isValidPassword(String password) {
        return false;
    }
}
