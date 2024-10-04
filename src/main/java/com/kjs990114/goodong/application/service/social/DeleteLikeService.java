package com.kjs990114.goodong.application.service.social;

import com.kjs990114.goodong.application.port.in.social.DeleteLikeUseCase;
import com.kjs990114.goodong.application.port.out.db.DeleteLikePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteLikeService implements DeleteLikeUseCase {
    private final DeleteLikePort deleteLikePort;
    @Override
    public void deleteLike(DeleteLikeCommand deleteLikeCommand) {
        deleteLikePort.delete(deleteLikeCommand.getPostId(), deleteLikeCommand.getUserId());
    }
}
