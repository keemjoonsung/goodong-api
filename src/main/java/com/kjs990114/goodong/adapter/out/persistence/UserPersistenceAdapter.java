package com.kjs990114.goodong.adapter.out.persistence;

import com.kjs990114.goodong.adapter.out.persistence.entity.UserEntity;
import com.kjs990114.goodong.adapter.out.persistence.repository.UserRepository;
import com.kjs990114.goodong.application.port.out.CreateUserPort;
import com.kjs990114.goodong.application.port.out.LoadUserPort;
import com.kjs990114.goodong.domain.user.User;
import com.kjs990114.goodong.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements CreateUserPort, LoadUserPort {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        return UserMapper.toDomain(userRepository.save(userEntity));
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return false;
    }
}
