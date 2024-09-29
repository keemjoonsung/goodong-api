package com.kjs990114.goodong.application.service.auth;

import com.kjs990114.goodong.application.port.in.auth.WithDrawUseCase;
import com.kjs990114.goodong.application.port.out.db.DeleteUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithDrawService implements WithDrawUseCase {
    private final DeleteUserPort deleteUserPort;

    @Override
    public void withdraw(WithDrawCommand withDrawCommand) {
        deleteUserPort.deleteByUserId(withDrawCommand.getUserId());
    }
}
