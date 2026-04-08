package com.kiwi.kiwiserver.domain.identity.user.service;

import com.kiwi.kiwiserver.domain.identity.user.dto.request.UpdateNicknameRequest;
import com.kiwi.kiwiserver.domain.item.exception.ItemErrorCode;
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

    @Transactional
    public UserResponse updateMyNickname(UpdateNicknameRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        // 현재 로그인한 사용자의 닉네임 수정
        user.updateNickname(request.getNickname());

        return userMapper.toResponse(user);
    }

    private void decreaseKiwiBalance(User user, int amount) {
        if (user.getKiwiBalance() < amount) {
            throw new BusinessException(ItemErrorCode.INSUFFICIENT_KIWI_BALANCE);
        }

        user.updateKiwiBalance(user.getKiwiBalance() - amount);
    }
}