package com.kiwi.kiwiserver.domain.identity.account.service;

import com.kiwi.kiwiserver.domain.identity.account.dto.request.ChangePasswordRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.request.DeleteAccountRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.request.LoginRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.request.RefreshTokenRequest;
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

    public LoginResponse login(LoginRequest request) {
        // 이메일에 해당하는 계정이 없는 경우
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.INVALID_CREDENTIALS));

        // 탈퇴한 계정인 경우
        if (Boolean.TRUE.equals(account.getIsDeleted())) {
            throw new BusinessException(AccountErrorCode.DELETED_ACCOUNT);
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

    private void validateDuplicateEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new BusinessException(AccountErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}
