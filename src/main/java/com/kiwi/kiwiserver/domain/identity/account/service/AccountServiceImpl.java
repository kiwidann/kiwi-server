package com.kiwi.kiwiserver.domain.identity.account.service;

import com.kiwi.kiwiserver.domain.identity.account.dto.request.CreateAccountRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.response.AccountResponse;
import com.kiwi.kiwiserver.domain.identity.account.entity.Account;
import com.kiwi.kiwiserver.domain.identity.account.exception.AccountErrorCode;
import com.kiwi.kiwiserver.domain.identity.account.mapper.AccountMapper;
import com.kiwi.kiwiserver.domain.identity.account.repository.AccountRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AccountResponse signUp(CreateAccountRequest request) {
        // 중복된 이메일인지 검증
        validateDuplicateEmail(request.getEmail());

        // 비밀번호 해시 처리
        String passwordHash = passwordEncoder.encode(request.getPassword());

        Account account = accountMapper.toEntity(request, passwordHash);
        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponse(savedAccount);
    }

    private void validateDuplicateEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new BusinessException(AccountErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}
