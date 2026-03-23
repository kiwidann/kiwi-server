package com.kiwi.kiwiserver.domain.identity.account.service;

import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private static final String PREFIX = "email-verification:";
    private static final long EXPIRATION_MINUTES = 5;

    private final StringRedisTemplate stringRedisTemplate;

    public String generateAndSaveCode(String email) {
        String code = generateCode();

        stringRedisTemplate.opsForValue().set(
                createKey(email),
                code,
                Duration.ofMinutes(EXPIRATION_MINUTES)
        );

        return code;
    }

    public String findCode(String email) {
        return stringRedisTemplate.opsForValue().get(createKey(email));
    }

    public boolean matches(String email, String code) {
        String savedCode = findCode(email);
        return savedCode != null && savedCode.equals(code);
    }

    public void delete(String email) {
        stringRedisTemplate.delete(createKey(email));
    }

    private String createKey(String email) {
        return PREFIX + email;
    }

    private String generateCode() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }
}
