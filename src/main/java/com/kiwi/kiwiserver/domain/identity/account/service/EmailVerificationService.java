package com.kiwi.kiwiserver.domain.identity.account.service;

import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    // Redis key prefix
    // 실제 인증 코드 저장용
    private static final String CODE_PREFIX = "email-verification:";
    // 인증 코드 재전송 제한용
    private static final String COOLDOWN_PREFIX = "email-verification:cooldown:";
    // 인증 코드 입력 실패 횟수 저장용
    private static final String ATTEMPT_PREFIX = "email-verification:attempt:";

    private static final long CODE_EXPIRATION_MINUTES = 5;
    private static final long COOLDOWN_SECONDS = 60;
    private static final int MAX_ATTEMPTS = 5;

    private final StringRedisTemplate stringRedisTemplate;

    // 재전송 제한 시간이 아직 남아있는지 확인
    public boolean isCooldownActive(String email) {
        Boolean exists = stringRedisTemplate.hasKey(createCooldownKey(email));
        return Boolean.TRUE.equals(exists);
    }

    // 인증 코드 저장, 재전송 제한 설정, 기존 시도 횟수 초기화를 한 번에 수행
    public String generateAndSaveCode(String email) {
        String code = generateCode();

        // 인증 코드는 5분간 유효
        stringRedisTemplate.opsForValue().set(
                createCodeKey(email),
                code,
                Duration.ofMinutes(CODE_EXPIRATION_MINUTES)
        );

        // 인증 코드 발송 후 60초 동안은 재전송 제한
        stringRedisTemplate.opsForValue().set(
                createCooldownKey(email),
                "1",
                Duration.ofSeconds(COOLDOWN_SECONDS)
        );

        // 새 코드 발급 시 기존 시도 횟수 초기화
        stringRedisTemplate.delete(createAttemptKey(email));

        return code;
    }

    public String findCode(String email) {
        return stringRedisTemplate.opsForValue().get(createCodeKey(email));
    }

    public boolean matches(String email, String code) {
        String savedCode = findCode(email);
        return savedCode != null && savedCode.equals(code);
    }

    // 인증 코드 입력 시도 횟수가 제한을 초과했는지 확인
    public boolean isAttemptExceeded(String email) {
        String count = stringRedisTemplate.opsForValue().get(createAttemptKey(email));
        if (count == null) {
            return false;
        }

        return Integer.parseInt(count) >= MAX_ATTEMPTS;
    }

    // 인증 코드 검증 실패 시 시도 횟수 1 증가
    public long increaseAttempt(String email) {
        Long count = stringRedisTemplate.opsForValue().increment(createAttemptKey(email));

        // 첫 시도라면 인증코드 만료 시간과 비슷하게 TTL 설정
        if (count != null && count == 1L) {
            stringRedisTemplate.expire(
                    createAttemptKey(email),
                    Duration.ofMinutes(CODE_EXPIRATION_MINUTES)
            );
        }

        return count == null ? 0 : count;
    }

    // 인증 성공 후 관련 Redis 데이터(코드, 시도 횟수, 재전송 제한) 정리
    public void clearVerificationData(String email) {
        stringRedisTemplate.delete(createCodeKey(email));
        stringRedisTemplate.delete(createAttemptKey(email));
        stringRedisTemplate.delete(createCooldownKey(email));
    }

    private String createCodeKey(String email) {
        return CODE_PREFIX + email;
    }

    private String createCooldownKey(String email) {
        return COOLDOWN_PREFIX + email;
    }

    private String createAttemptKey(String email) {
        return ATTEMPT_PREFIX + email;
    }

    private String generateCode() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }
}