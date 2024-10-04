package com.kjs990114.goodong.application.service.social;

import com.kjs990114.goodong.application.port.in.social.AddFollowUseCase;
import com.kjs990114.goodong.application.port.out.db.SaveFollowPort;
import com.kjs990114.goodong.domain.follow.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddFollowService implements AddFollowUseCase {

    private final SaveFollowPort saveFollowPort;

    @Transactional
    @Override
    public void addFollow(AddFollowCommand addFollowCommand) {
        Long followerId = addFollowCommand.getFollowerId();
        Long followeeId = addFollowCommand.getFolloweeId();
        Follow follow = Follow.of(followerId,followeeId);
        saveFollowPort.save(follow);
    }
}
