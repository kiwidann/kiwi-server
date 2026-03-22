package com.kiwi.kiwiserver.domain.identity.user.service;

import com.kiwi.kiwiserver.global.exception.BusinessException;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import com.kiwi.kiwiserver.domain.identity.user.dto.response.UserResponse;
import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.domain.identity.user.exception.UserErrorCode;
import com.kiwi.kiwiserver.domain.identity.user.mapper.UserMapper;
import com.kiwi.kiwiserver.domain.identity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getMyInfo() {
        Long userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        return userMapper.toResponse(user);
    }
}