package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.controller;

import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.request.ThinkingToolCreateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.dto.response.*;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity.ThinkingToolCode;
import com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.service.ThinkingToolService;
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
@RequestMapping("/api/thinking-tools")
@Tag(name = "Thinking Tool", description = "생각 정리 도구 API")
public class ThinkingToolController {

    private final ThinkingToolService thinkingToolService;

    @GetMapping("/tags")
    @Operation(summary = "생각 정리 도구 태그 목록 조회")
    public ApiResponse<List<TagResponse>> getTags() {
        return ApiResponse.success(thinkingToolService.getTags());
    }

    @GetMapping("/questions")
    @Operation(summary = "생각 정리 도구 질문 목록 조회")
    public ApiResponse<List<ThinkingToolQuestionResponse>> getQuestions(
            @RequestParam ThinkingToolCode toolCode
    ) {
        return ApiResponse.success(thinkingToolService.getQuestions(toolCode));
    }

    @PostMapping("/records/{recordId}")
    @Operation(summary = "생각 정리 도구 세션 생성")
    public ApiResponse<ThinkingToolSessionResponse> createThinkingTool(
            @PathVariable Long recordId,
            @Valid @RequestBody ThinkingToolCreateRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(thinkingToolService.createThinkingTool(userId, recordId, request));
    }

    @GetMapping("/records/{recordId}")
    @Operation(summary = "특정 기록의 생각 정리 도구 세션 목록 조회")
    public ApiResponse<List<ThinkingToolSessionSummaryResponse>> getThinkingToolSessions(
            @PathVariable Long recordId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(thinkingToolService.getThinkingToolSessions(userId, recordId));
    }

    @GetMapping("/records/{recordId}/{thinkingToolSessionId}")
    @Operation(summary = "생각 정리 도구 세션 상세 조회")
    public ApiResponse<ThinkingToolSessionResponse> getThinkingToolSession(
            @PathVariable Long recordId,
            @PathVariable Long thinkingToolSessionId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(thinkingToolService.getThinkingToolSession(userId, recordId, thinkingToolSessionId));
    }
}