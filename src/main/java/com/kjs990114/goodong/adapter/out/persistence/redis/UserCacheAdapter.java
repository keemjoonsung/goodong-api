package com.kjs990114.goodong.adapter.out.persistence.redis;

import com.kjs990114.goodong.application.dto.UserSummaryDTO;
import com.kjs990114.goodong.application.port.out.cache.DeleteUserCachePort;
import com.kjs990114.goodong.application.port.out.cache.LoadUserCachePort;
import com.kjs990114.goodong.application.port.out.cache.SaveUserCachePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCacheAdapter implements LoadUserCachePort , SaveUserCachePort , DeleteUserCachePort {

    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public UserSummaryDTO loadUserDTO(Long userId, String token) {
        return (UserSummaryDTO) redisTemplate.opsForHash().get("user:" + userId + ":tokens", token);
    }

    @Override
    public void saveUserDTO(Long userId, String token, UserSummaryDTO userSummaryDTO) {
        redisTemplate.opsForHash().put("user:" + userId + ":tokens", token, userSummaryDTO);
    }

    @Override
    public void deleteUserDTO(Long userId) {
        redisTemplate.delete("user:" + userId + ":tokens");
    }
}
