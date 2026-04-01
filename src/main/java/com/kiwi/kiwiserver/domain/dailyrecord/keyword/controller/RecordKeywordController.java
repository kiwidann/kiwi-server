package com.kiwi.kiwiserver.domain.dailyrecord.keyword.controller;

import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.request.RecordKeywordUpdateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.dto.response.KeywordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.keyword.service.RecordKeywordService;
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
@RequestMapping("/api/records/{recordId}/keywords")
@Tag(name = "RecordKeyword", description = "기록-키워드 API")
public class RecordKeywordController {

    private final RecordKeywordService recordKeywordService;

    @GetMapping
    @Operation(summary = "특정 기록의 키워드 조회")
    public ApiResponse<List<KeywordResponse>> getRecordKeywords(
            @PathVariable Long recordId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(recordKeywordService.getRecordKeywords(userId, recordId));
    }

    @PutMapping
    @Operation(summary = "특정 기록의 키워드 목록 저장/교체")
    public ApiResponse<List<KeywordResponse>> replaceRecordKeywords(
            @PathVariable Long recordId,
            @Valid @RequestBody RecordKeywordUpdateRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(recordKeywordService.replaceRecordKeywords(userId, recordId, request));
    }
}