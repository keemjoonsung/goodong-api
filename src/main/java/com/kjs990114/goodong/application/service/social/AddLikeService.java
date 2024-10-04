package com.kjs990114.goodong.application.service.social;

import com.kjs990114.goodong.application.port.in.social.AddLikeUseCase;
import com.kjs990114.goodong.application.port.out.db.SaveLikePort;
import com.kjs990114.goodong.domain.like.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddLikeService implements AddLikeUseCase {

    private final SaveLikePort saveLikePort;

    @Transactional
    @Override
    public void addLike(AddLikeCommand addLikeCommand) {
        Like like = Like.of(addLikeCommand.getPostId(),addLikeCommand.getUserId());
        saveLikePort.save(like);
    }
}
