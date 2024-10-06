package com.kjs990114.goodong.application.service.social;

import com.kjs990114.goodong.application.port.in.social.DeleteFollowUseCase;
import com.kjs990114.goodong.application.port.out.db.DeleteFollowPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteFollowService implements DeleteFollowUseCase {

    private final DeleteFollowPort deleteFollowPort;

    @Transactional
    @Override
    public void deleteFollow(DeleteFollowCommand deleteFollowCommand) {
        Long followerId = deleteFollowCommand.getFollowerId();
        Long followeeId = deleteFollowCommand.getFolloweeId();
        deleteFollowPort.delete(followerId,followeeId);
    }
}
