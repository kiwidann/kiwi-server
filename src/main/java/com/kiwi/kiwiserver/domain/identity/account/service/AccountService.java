package com.kiwi.kiwiserver.domain.identity.account.service;

import com.kiwi.kiwiserver.domain.identity.account.dto.request.*;
import com.kiwi.kiwiserver.domain.identity.account.dto.response.LoginResponse;
import com.kiwi.kiwiserver.domain.identity.account.dto.response.RefreshTokenResponse;
import com.kiwi.kiwiserver.domain.identity.account.entity.Account;
import com.kiwi.kiwiserver.domain.identity.account.exception.AccountErrorCode;
import com.kiwi.kiwiserver.domain.identity.account.mapper.AccountMapper;
import com.kiwi.kiwiserver.domain.identity.account.repository.AccountRepository;
import com.kiwi.kiwiserver.domain.identity.common.dto.request.SignUpRequest;
import com.kiwi.kiwiserver.domain.identity.common.dto.response.SignUpResponse;
import com.kiwi.kiwiserver.domain.identity.common.mapper.SignUpMapper;
import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.domain.identity.user.mapper.UserMapper;
import com.kiwi.kiwiserver.domain.identity.user.repository.UserRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import com.kiwi.kiwiserver.global.mail.MailService;
import com.kiwi.kiwiserver.global.security.jwt.JwtProvider;
import com.kiwi.kiwiserver.global.security.refresh.RefreshTokenService;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final UserMapper userMapper; 
    private final SignUpMapper signUpMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final EmailVerificationService emailVerificationService;
    private final MailService mailService;
    private final PasswordResetService passwordResetService;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        // 이메일 중복 검증
        validateDuplicateEmail(request.getEmail());

        // 비밀번호 해시 처리
        String passwordHash = passwordEncoder.encode(request.getPassword());

        Account account = accountMapper.toEntity(request, passwordHash);
        Account savedAccount = accountRepository.save(account);

        User user = userMapper.toEntity(request, savedAccount);
        User savedUser = userRepository.save(user);

        return signUpMapper.toResponse(savedAccount, savedUser);
    }

    @Transactional
    public void sendVerificationEmail(SendVerificationEmailRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        if (Boolean.TRUE.equals(account.getIsDeleted())) {
            throw new BusinessException(AccountErrorCode.DELETED_ACCOUNT);
        }

        // 이미 인증된 계정은 재인증 불필요
        if (Boolean.TRUE.equals(account.getIsVerified())) {
            throw new BusinessException(AccountErrorCode.ALREADY_VERIFIED_ACCOUNT);
        }

        // 짧은 시간 내 반복 발송 요청 방지
        if (emailVerificationService.isCooldownActive(account.getEmail())) {
            throw new BusinessException(AccountErrorCode.TOO_MANY_VERIFICATION_REQUESTS);
        }

        // 인증 코드 생성 및 Redis 저장
        String code = emailVerificationService.generateAndSaveCode(account.getEmail());

        // 사용자 이메일로 인증 코드 전송
        mailService.sendVerificationCode(account.getEmail(), code);
    }

    @Transactional
    public void verifyEmailCode(VerifyEmailCodeRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        // 탈퇴한 계정은 인증 처리 불가
        if (Boolean.TRUE.equals(account.getIsDeleted())) {
            throw new BusinessException(AccountErrorCode.DELETED_ACCOUNT);
        }

        // 이미 인증된 계정은 다시 인증할 필요 없음
        if (Boolean.TRUE.equals(account.getIsVerified())) {
            throw new BusinessException(AccountErrorCode.ALREADY_VERIFIED_ACCOUNT);
        }

        // Redis에 인증 코드가 없으면 만료되었거나 발급되지 않은 상태
        String savedCode = emailVerificationService.findCode(account.getEmail());
        if (savedCode == null) {
            throw new BusinessException(AccountErrorCode.VERIFICATION_CODE_NOT_FOUND);
        }

        // brute-force 방지를 위해 시도 횟수 초과 여부 확인
        if (emailVerificationService.isAttemptExceeded(account.getEmail())) {
            throw new BusinessException(AccountErrorCode.TOO_MANY_VERIFICATION_ATTEMPTS);
        }

        // 인증 코드가 틀리면 실패 횟수 증가
        if (!emailVerificationService.matches(account.getEmail(), request.getCode())) {
            emailVerificationService.increaseAttempt(account.getEmail());
            throw new BusinessException(AccountErrorCode.INVALID_VERIFICATION_CODE);
        }

        // 인증 완료 처리
        account.verify(OffsetDateTime.now());

        // 인증 성공 후 Redis에 남아 있는 관련 데이터 정리
        emailVerificationService.clearVerificationData(account.getEmail());
    }

    public LoginResponse login(LoginRequest request) {
        // 이메일에 해당하는 계정이 없는 경우
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.INVALID_CREDENTIALS));

        // 탈퇴한 계정인 경우
        if (Boolean.TRUE.equals(account.getIsDeleted())) {
            throw new BusinessException(AccountErrorCode.DELETED_ACCOUNT);
        }

        // 이메일 인증이 완료되지 않은 경우
        if (!Boolean.TRUE.equals(account.getIsVerified())) {
            throw new BusinessException(AccountErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 비밀번호가 일치하지 않은 경우
        if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            throw new BusinessException(AccountErrorCode.INVALID_CREDENTIALS);
        }

        // 계정에 연결된 사용자 정보가 없는 경우
        User user = userRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        // access token 생성
        String accessToken = jwtProvider.generateAccessToken(
                account.getAccountId(),
                user.getUserId(),
                account.getEmail()
        );

        // refresh token 생성
        String refreshToken = jwtProvider.generateRefreshToken(account.getAccountId());

        // Redis에 refresh token 저장
        refreshTokenService.save(
                account.getAccountId(),
                refreshToken,
                jwtProvider.getRefreshTokenExpirationMs()
        );

        return LoginResponse.builder()
                .accountId(account.getAccountId())
                .userId(user.getUserId())
                .email(account.getEmail())
                .nickname(user.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // access token 재발급 함수
    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // 요청으로 들어온 refresh token의 유효성 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new BusinessException(AccountErrorCode.INVALID_REFRESH_TOKEN);
        }

        // refresh token에서 accountId 추출
        Long accountId = jwtProvider.getAccountId(refreshToken);

        // Redis에 저장된 refresh token과 일치하는지 확인
        if (!refreshTokenService.matches(accountId, refreshToken)) {
            throw new BusinessException(AccountErrorCode.INVALID_REFRESH_TOKEN);
        }

        // account 테이블에 존재하는 계정인지 확인
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        // 탈퇴한 계정인지 확인
        if (Boolean.TRUE.equals(account.getIsDeleted())) {
            throw new BusinessException(AccountErrorCode.DELETED_ACCOUNT);
        }

        // 계정에 연결된 사용자 정보가 있는지 확인
        User user = userRepository.findByAccount_AccountId(account.getAccountId())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        // 새로운 access token 발급
        String newAccessToken = jwtProvider.generateAccessToken(
                account.getAccountId(),
                user.getUserId(),
                account.getEmail()
        );

        // refresh token 재사용을 허용하면서 만료 시간만 연장
        refreshTokenService.extendExpiration(accountId, jwtProvider.getRefreshTokenExpirationMs());

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    public void logout() {
        Long accountId = SecurityUtils.getCurrentAccountId();
        refreshTokenService.delete(accountId);   // Redis에 저장된 refresh token 삭제
    }

    @Transactional
    public void deleteMyAccount(DeleteAccountRequest request) {
        Long accountId = SecurityUtils.getCurrentAccountId();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        if (Boolean.TRUE.equals(account.getIsDeleted())) {
            throw new BusinessException(AccountErrorCode.ALREADY_DELETED_ACCOUNT);
        }

        if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            throw new BusinessException(AccountErrorCode.INVALID_PASSWORD);
        }

        account.softDelete(OffsetDateTime.now());
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        Long accountId = SecurityUtils.getCurrentAccountId();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        if (Boolean.TRUE.equals(account.getIsDeleted())) {
            throw new BusinessException(AccountErrorCode.DELETED_ACCOUNT);
        }

        // 현재 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPasswordHash())) {
            throw new BusinessException(AccountErrorCode.INVALID_PASSWORD);
        }

        // 새 비밀번호가 현재 비밀번호와 같은지 확인
        if (passwordEncoder.matches(request.getNewPassword(), account.getPasswordHash())) {
            throw new BusinessException(AccountErrorCode.PASSWORD_SAME_AS_OLD);
        }

        // 새 비밀번호 해시 처리 후 저장
        String newPasswordHash = passwordEncoder.encode(request.getNewPassword());
        account.updatePasswordHash(newPasswordHash);

        // 보안상 기존 refresh token 제거
        refreshTokenService.delete(accountId);
    }

    // 비밀번호 재설정은 인증된 이메일 계정에 대해서 허용
    @Transactional
    public void sendResetPasswordCode(SendResetPasswordCodeRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        if (Boolean.TRUE.equals(account.getIsDeleted())) {
            throw new BusinessException(AccountErrorCode.DELETED_ACCOUNT);
        }

        if (!Boolean.TRUE.equals(account.getIsVerified())) {
            throw new BusinessException(AccountErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 재전송 제한 확인
        if (emailVerificationService.isCooldownActive(account.getEmail())) {
            throw new BusinessException(AccountErrorCode.TOO_MANY_VERIFICATION_REQUESTS);
        }

        // 인증 코드 생성 및 저장
        String code = emailVerificationService.generateAndSaveCode(account.getEmail());

        // 비밀번호 재설정용 인증 코드 메일 발송
        mailService.sendVerificationCode(account.getEmail(), code);
    }

    // 인증 코드 검증 성공 시 비밀번호 재설정 가능 상태를 Redis에 저장
    @Transactional
    public void verifyResetPasswordCode(VerifyResetPasswordCodeRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        if (Boolean.TRUE.equals(account.getIsDeleted())) {
            throw new BusinessException(AccountErrorCode.DELETED_ACCOUNT);
        }

        String savedCode = emailVerificationService.findCode(account.getEmail());
        if (savedCode == null) {
            throw new BusinessException(AccountErrorCode.VERIFICATION_CODE_NOT_FOUND);
        }

        // brute-force 방지를 위해 시도 횟수 초과 여부 확인
        if (emailVerificationService.isAttemptExceeded(account.getEmail())) {
            throw new BusinessException(AccountErrorCode.TOO_MANY_VERIFICATION_ATTEMPTS);
        }

        // 인증 코드가 틀리면 실패 횟수 증가
        if (!emailVerificationService.matches(account.getEmail(), request.getCode())) {
            emailVerificationService.increaseAttempt(account.getEmail());
            throw new BusinessException(AccountErrorCode.INVALID_VERIFICATION_CODE);
        }

        // 비밀번호 재설정 가능 상태 부여
        passwordResetService.markResetAllowed(account.getEmail());

        // 인증 코드 관련 Redis 데이터 삭제
        emailVerificationService.clearVerificationData(account.getEmail());
    }

    // 재설정 코드 검증이 끝난 이메일만 비밀번호 재설정 가능
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        if (Boolean.TRUE.equals(account.getIsDeleted())) {
            throw new BusinessException(AccountErrorCode.DELETED_ACCOUNT);
        }

        // 재설정 인증 완료 상태인지 확인
        if (!passwordResetService.isResetAllowed(account.getEmail())) {
            throw new BusinessException(AccountErrorCode.RESET_PASSWORD_NOT_ALLOWED);
        }

        // 기존 비밀번호와 동일한지 확인
        if (passwordEncoder.matches(request.getNewPassword(), account.getPasswordHash())) {
            throw new BusinessException(AccountErrorCode.PASSWORD_SAME_AS_OLD);
        }

        // 새 비밀번호 해시 후 저장
        String newPasswordHash = passwordEncoder.encode(request.getNewPassword());
        account.updatePasswordHash(newPasswordHash);

        // 기존 refresh token 제거
        refreshTokenService.delete(account.getAccountId());

        // 재설정 가능 상태 삭제
        passwordResetService.clearResetAllowed(account.getEmail());
    }

    private void validateDuplicateEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new BusinessException(AccountErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}
