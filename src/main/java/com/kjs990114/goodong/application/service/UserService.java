package com.kjs990114.goodong.application.service;

import com.kjs990114.goodong.adapter.out.persistence.entity.UserEntity;
import com.kjs990114.goodong.common.exception.NotFoundException;
import com.kjs990114.goodong.adapter.out.persistence.repository.UserRepository;
import com.kjs990114.goodong.adapter.in.web.dto.DTOMapper;
import com.kjs990114.goodong.adapter.in.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileService fileService;
    private final FollowService followService;
    private final HandlerMapping resourceHandlerMapping;
    @Value("${spring.cloud.gcp.storage.path}")
    private String storagePath;
    private final RedisTemplate<String,Object> redisTemplate;


    @Transactional(readOnly = true)
    public UserDTO.UserDetail getUserInfoDetail(Long userId) {
        String key =  "userDetail:" + userId;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        UserDTO.UserDetail cachedInfo = (UserDTO.UserDetail) valueOperations.get(key);
        if(cachedInfo != null){
            System.out.println("유저정보 캐시 히트");
            return cachedInfo;
        }
        System.out.println("유저 정보 캐시 미스");
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        UserDTO.UserDetail dbInfo = DTOMapper.userToDetail(userEntity);
        valueOperations.set(key,dbInfo);
        return dbInfo;
    }
    @Transactional(readOnly = true)
    public UserDTO.UserSummary getUserInfoSummary(Long userId) {
        ValueOperations<String, Object> valueOp = redisTemplate.opsForValue();
        String key =   "userSummary:" + userId;
        UserDTO.UserSummary cachedInfo = (UserDTO.UserSummary) valueOp.get(key);
        if(cachedInfo != null){
            System.out.println("유저 header 캐시 히트");
            return cachedInfo;
        }

        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        System.out.println("유저 header 캐시 미스");
        UserDTO.UserSummary dbInfo = DTOMapper.userToSummary(userEntity);
        valueOp.set(key,dbInfo);
        return dbInfo;
    }

    @Transactional
    public void updateUserProfile(Long userId, UserDTO.UpdateUser update) throws IOException {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        if (update.getNickname() != null) {
            updateUserNickname(userEntity, update.getNickname());
        }
        if (update.getProfileImage() != null) {
            MultipartFile file = update.getProfileImage();
            String fileName = fileService.saveFileStorage(file, FileService.Extension.PNG);
            String profileImageUrl = storagePath + fileName;
            updateProfileImage(userEntity, profileImageUrl);
        }
        userRepository.save(userEntity);
        redisTemplate.delete("userSummary:" + userId);
        redisTemplate.delete("userDetail:" + userId);
        redisTemplate.delete("userPosts" + userId);
    }

    @Transactional
    public void deleteUser(Long userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        userEntity.softDelete();
        userRepository.save(userEntity);
        redisTemplate.delete("userSummary:" + userId);
        redisTemplate.delete("userDetail:" + userId);
        redisTemplate.delete("userPosts" + userId);
    }

    @Transactional(readOnly = true)
    public List<UserDTO.UserContribution> getContributionList(Long userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User does not exists"));
        return userEntity.getContributions().stream().map(
                contribution -> new UserDTO.UserContribution(contribution.getDate(), contribution.getCount())
        ).toList();
    }


    private void updateProfileImage(UserEntity userEntity, String profileImageUrl) {
        userEntity.updateProfileImage(profileImageUrl);
    }

    private void updateUserNickname(UserEntity userEntity, String nickname) {
        userEntity.updateNickname(nickname);
    }

}



