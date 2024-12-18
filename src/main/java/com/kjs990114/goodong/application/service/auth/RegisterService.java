package com.kjs990114.goodong.application.service.auth;

import com.kjs990114.goodong.application.port.in.auth.RegisterUseCase;
import com.kjs990114.goodong.application.port.out.db.SaveUserPort;
import com.kjs990114.goodong.application.port.out.db.LoadUserPort;
import com.kjs990114.goodong.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {

    private final SaveUserPort saveUserPort;
    private final LoadUserPort loadUserPort;
    private final PasswordEncoder passwordEncoder;
    @Value("${default.profile.images}")
    private String[] defaultImages;

    @Transactional
    @Override
    public void register(RegisterCommand registerCommand) {
        User user = User.builder()
                .email(registerCommand.getEmail())
                .nickname(registerCommand.getNickname())
                .build();
        user.changeProfileImage(defaultImages[Math.abs(user.getEmail().hashCode() % 10)]);
        user.changePassword(passwordEncoder.encode(registerCommand.getPassword()));
        saveUserPort.save(user);
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
