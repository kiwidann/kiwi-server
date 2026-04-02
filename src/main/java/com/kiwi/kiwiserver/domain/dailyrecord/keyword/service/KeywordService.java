package com.kiwi.kiwiserver.domain.dailyrecord.keyword.service;

import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.request.KeywordCreateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.request.KeywordUpdateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.response.KeywordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.entity.Keyword;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.exception.KeywordErrorCode;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.mapper.KeywordMapper;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.repository.KeywordRepository;
import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.domain.identity.user.repository.UserRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kiwi.kiwiserver.domain.identity.user.exception.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final KeywordMapper keywordMapper;

    public List<KeywordResponse> getMyKeywords(Long userId) {
        return keywordRepository.findAllByUser_UserIdOrderByKeywordIdAsc(userId)
                .stream()
                .map(keywordMapper::toResponse)
                .toList();
    }

    @Transactional
    public KeywordResponse createKeyword(Long userId, KeywordCreateRequest request) {
        String normalizedName = normalizeName(request.name());

        if (keywordRepository.existsByUser_UserIdAndName(userId, normalizedName)) {
            throw new BusinessException(KeywordErrorCode.KEYWORD_ALREADY_EXISTS);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Keyword keyword = Keyword.builder()
                .user(user)
                .name(normalizedName)
                .build();

        Keyword savedKeyword = keywordRepository.save(keyword);
        return keywordMapper.toResponse(savedKeyword);
    }

    @Transactional
    public KeywordResponse updateKeyword(Long userId, Long keywordId, KeywordUpdateRequest request) {
        Keyword keyword = keywordRepository.findByKeywordIdAndUser_UserId(keywordId, userId)
                .orElseThrow(() -> new BusinessException(KeywordErrorCode.KEYWORD_NOT_FOUND));

        String newName = request.name().trim();

        if (keywordRepository.existsByUser_UserIdAndName(userId, newName)) {
            throw new BusinessException(KeywordErrorCode.KEYWORD_ALREADY_EXISTS);
        }

        keyword.updateName(newName);

        return keywordMapper.toResponse(keyword);
    }

    private String normalizeName(String name) {
        String normalized = name == null ? null : name.trim();

        if (normalized == null || normalized.isBlank()) {
            throw new BusinessException(KeywordErrorCode.INVALID_KEYWORD_REQUEST);
        }

        return normalized;
    }
}