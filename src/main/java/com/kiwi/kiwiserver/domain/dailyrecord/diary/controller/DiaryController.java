package com.kiwi.kiwiserver.domain.dailyrecord.diary.controller;

import com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.request.DiaryCreateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.request.DiaryUpdateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.dto.response.DiaryResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.diary.service.DiaryService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
@Tag(name = "Diary", description = "일기 API")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    @Operation(summary = "일기 생성")
    public ApiResponse<DiaryResponse> createDiary(
            @Valid @RequestBody DiaryCreateRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(diaryService.createDiary(userId, request));
    }

    @GetMapping("/records/{recordId}")
    @Operation(summary = "특정 기록의 일기 조회")
    public ApiResponse<DiaryResponse> getDiaryByRecord(
            @PathVariable Long recordId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(diaryService.getDiaryByRecord(userId, recordId));
    }

    @PatchMapping("/records/{recordId}")
    @Operation(summary = "일기 수정")
    public ApiResponse<DiaryResponse> updateDiary(
            @PathVariable Long recordId,
            @Valid @RequestBody DiaryUpdateRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(diaryService.updateDiary(userId, recordId, request));
    }

    @DeleteMapping("/records/{recordId}")
    @Operation(summary = "일기 삭제")
    public ApiResponse<Void> deleteDiary(
            @PathVariable Long recordId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        diaryService.deleteDiary(userId, recordId);
        return ApiResponse.success(null);
    }

    @GetMapping
    @Operation(summary = "내 일기 목록 조회")
    public ApiResponse<Page<DiaryResponse>> getMyDiaries(
            @PageableDefault(size = 10, sort = "record.recordDate", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(diaryService.getMyDiaries(userId, pageable));
    }
}