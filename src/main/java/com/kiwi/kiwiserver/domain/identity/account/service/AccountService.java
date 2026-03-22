package com.kiwi.kiwiserver.domain.identity.account.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private void validateDuplicateEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new BusinessException(AccountErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}
