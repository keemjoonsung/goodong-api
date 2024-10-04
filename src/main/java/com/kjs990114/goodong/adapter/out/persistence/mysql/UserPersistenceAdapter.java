package com.kjs990114.goodong.adapter.out.persistence.mysql;

import com.kjs990114.goodong.adapter.out.persistence.mysql.entity.UserEntity;
import com.kjs990114.goodong.adapter.out.persistence.mysql.mapper.UserMapper;
import com.kjs990114.goodong.adapter.out.persistence.mysql.repository.UserRepository;
import com.kjs990114.goodong.application.dto.UserDetailDTO;
import com.kjs990114.goodong.application.port.out.db.DeleteUserPort;
import com.kjs990114.goodong.application.port.out.db.LoadUserPort;
import com.kjs990114.goodong.application.port.out.db.SaveUserPort;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements SaveUserPort, LoadUserPort , DeleteUserPort {

    private final UserRepository userRepository;

    @Override
    public Long save(User user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        return userRepository.save(userEntity).getUserId();
    }
    @Override
    public void deleteByUserId(Long userId){
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        userEntity.softDelete();
        userRepository.save(userEntity);
    }

    @Override
    public User loadByUserId(Long userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public User loadByUserEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User does not exists"));
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();

    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    @Override
    public UserDetailDTO loadUserInfoByUserIdBasedOnViewerId(Long userId, Long viewerId) {
        UserDetailDTO response =  userRepository.findUserInfoByUserIdAndViewerId(userId,viewerId);
        if(response.getUserId() == null){
            throw new NotFoundException("User not found");
        }
        return response;
    }


}
