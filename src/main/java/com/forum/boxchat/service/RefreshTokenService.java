package com.forum.boxchat.service;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    private static final long REFRESH_EXPIRE_DAYS = 7;

    private final RedisTemplate<String, String> redisTemplate;

    public RefreshTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String refreshToken, String username) {
        redisTemplate.opsForValue().set(
                key(refreshToken),
                username,
                REFRESH_EXPIRE_DAYS,
                TimeUnit.DAYS
        );
    }

    public String getUsername(String refreshToken) {
        return redisTemplate.opsForValue().get(key(refreshToken));
    }

    public void delete(String refreshToken) {
        redisTemplate.delete(key(refreshToken));
    }

    private String key(String token) {
        return "refresh_token:" + token;
    }



    public void blackListAcessToken(String jti, long ttl) {
        redisTemplate.opsForValue().set("blacklist:" + jti, "revoked", ttl, TimeUnit.SECONDS);
    }


    public boolean isAccessTokenBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + jti));
    }



}
