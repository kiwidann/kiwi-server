package com.kiwi.kiwiserver.domain.identity.account.service;

import com.kiwi.kiwiserver.domain.identity.account.dto.request.DeleteAccountRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.request.LoginRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.response.LoginResponse;
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

        // 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(
                account.getAccountId(),
                user.getUserId(),
                account.getEmail()
        );

        return LoginResponse.builder()
                .accountId(account.getAccountId())
                .userId(user.getUserId())
                .email(account.getEmail())
                .nickname(user.getNickname())
                .accessToken(accessToken)
                .build();
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

    private void validateDuplicateEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new BusinessException(AccountErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}
