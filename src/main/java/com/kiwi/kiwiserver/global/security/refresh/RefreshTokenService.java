package com.kiwi.kiwiserver.global.security.refresh;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final String PREFIX = "refresh:";

    private final StringRedisTemplate stringRedisTemplate;

    public void save(Long accountId, String refreshToken, long expirationMs) {
        stringRedisTemplate.opsForValue().set(
                createKey(accountId),
                refreshToken,
                Duration.ofMillis(expirationMs)
        );
    }

    public String findByAccountId(Long accountId) {
        return stringRedisTemplate.opsForValue().get(createKey(accountId));
    }

    public boolean matches(Long accountId, String refreshToken) {
        String savedRefreshToken = findByAccountId(accountId);
        return savedRefreshToken != null && savedRefreshToken.equals(refreshToken);
    }

    public void delete(Long accountId) {
        stringRedisTemplate.delete(createKey(accountId));
    }

    private String createKey(Long accountId) {
        return PREFIX + accountId;
    }
}
