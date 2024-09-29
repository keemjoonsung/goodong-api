package com.kjs990114.goodong.application.service.auth;

import com.kjs990114.goodong.application.port.in.auth.ChangePasswordUseCase;
import com.kjs990114.goodong.application.port.out.mysql.LoadUserPort;
import com.kjs990114.goodong.application.port.out.mysql.SaveUserPort;
import com.kjs990114.goodong.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordChangeService implements ChangePasswordUseCase {

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    @Override
    public void changePassword(PasswordQuery passwordQuery) {
        User user = loadUserPort.loadByUserId(passwordQuery.getUserId());
        user.changePassword(passwordQuery.getPassword());
        saveUserPort.save(user);
    }
}
