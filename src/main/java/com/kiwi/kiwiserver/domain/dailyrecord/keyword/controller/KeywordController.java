package com.kiwi.kiwiserver.domain.dailyrecord.keyword.controller;

import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.request.KeywordCreateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.request.KeywordUpdateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.response.KeywordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.service.KeywordService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/keywords")
@Tag(name = "Keyword", description = "키워드 API")
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping
    @Operation(summary = "내 키워드 목록 조회")
    public ApiResponse<List<KeywordResponse>> getMyKeywords() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(keywordService.getMyKeywords(userId));
    }

    @PostMapping
    @Operation(summary = "커스텀 키워드 생성")
    public ApiResponse<KeywordResponse> createKeyword(
            @Valid @RequestBody KeywordCreateRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(keywordService.createKeyword(userId, request));
    }

    @PatchMapping("/{keywordId}")
    @Operation(summary = "키워드 이름 변경")
    public ApiResponse<KeywordResponse> updateKeyword(
            @PathVariable Long keywordId,
            @Valid @RequestBody KeywordUpdateRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(keywordService.updateKeyword(userId, keywordId, request));
    }
}