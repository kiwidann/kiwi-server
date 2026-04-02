package com.kiwi.kiwiserver.domain.dailyrecord.cbt.controller;

import com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.request.CbtCreateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.dto.response.*;
import com.kiwi.kiwiserver.domain.dailyrecord.cbt.service.CbtService;
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
@RequestMapping("/api/cbt")
@Tag(name = "CBT", description = "CBT API")
public class CbtController {

    private final CbtService cbtService;

    @GetMapping("/tags")
    @Operation(summary = "CBT 태그 목록 조회")
    public ApiResponse<List<TagResponse>> getTags() {
        return ApiResponse.success(cbtService.getTags());
    }

    @GetMapping("/questions")
    @Operation(summary = "CBT 질문 목록 조회")
    public ApiResponse<List<CbtQuestionResponse>> getQuestions() {
        return ApiResponse.success(cbtService.getQuestions());
    }

    @PostMapping("/records/{recordId}")
    @Operation(summary = "CBT 세션 생성")
    public ApiResponse<CbtSessionResponse> createCbt(
            @PathVariable Long recordId,
            @Valid @RequestBody CbtCreateRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(cbtService.createCbt(userId, recordId, request));
    }

    @GetMapping("/records/{recordId}")
    @Operation(summary = "특정 기록의 CBT 세션 목록 조회")
    public ApiResponse<List<CbtSessionSummaryResponse>> getCbtSessions(
            @PathVariable Long recordId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(cbtService.getCbtSessions(userId, recordId));
    }

    @GetMapping("/records/{recordId}/{cbtSessionId}")
    @Operation(summary = "CBT 세션 상세 조회")
    public ApiResponse<CbtSessionResponse> getCbtSession(
            @PathVariable Long recordId,
            @PathVariable Long cbtSessionId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(cbtService.getCbtSession(userId, recordId, cbtSessionId));
    }
}