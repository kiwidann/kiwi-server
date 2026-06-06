package com.kiwi.kiwiserver.domain.identity.account.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private static final String PREFIX = "password-reset:verified:";
    private static final long EXPIRATION_MINUTES = 10;

    private final StringRedisTemplate stringRedisTemplate;

    // 비밀번호 재설정 가능 상태 저장
    public void markResetAllowed(String email) {
        stringRedisTemplate.opsForValue().set(
                createKey(email),
                "1",
                Duration.ofMinutes(EXPIRATION_MINUTES)
        );
    }

    // 비밀번호 재설정 가능 상태인지 확인
    public boolean isResetAllowed(String email) {
        Boolean exists = stringRedisTemplate.hasKey(createKey(email));
        return Boolean.TRUE.equals(exists);
    }

    // 비밀번호 재설정 완료 후 상태 삭제
    public void clearResetAllowed(String email) {
        stringRedisTemplate.delete(createKey(email));
    }

    private String createKey(String email) {
        return PREFIX + email;
    }
}
