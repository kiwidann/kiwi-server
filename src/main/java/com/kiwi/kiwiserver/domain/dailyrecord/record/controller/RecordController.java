package com.kiwi.kiwiserver.domain.dailyrecord.record.controller;

import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.request.RecordCreateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.request.RecordUpdateRequest;
import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response.MonthlyRecordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response.RecordDetailResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.record.dto.response.RecordResponse;
import com.kiwi.kiwiserver.domain.dailyrecord.record.service.RecordDetailService;
import com.kiwi.kiwiserver.domain.dailyrecord.record.service.RecordService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
@Tag(name = "Record", description = "하루 기록 API")
public class RecordController {

    private final RecordService recordService;
    private final RecordDetailService recordDetailService;

    @PostMapping
    @Operation(summary = "하루 기록 생성")
    public ApiResponse<RecordResponse> createRecord(
            @Valid @RequestBody RecordCreateRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(recordService.createRecord(userId, request));
    }

    @GetMapping("/{recordDate}")
    @Operation(summary = "날짜별 하루 기록 조회")
    public ApiResponse<RecordResponse> getRecordByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(recordService.getRecordByDate(userId, recordDate));
    }

    @GetMapping
    @Operation(summary = "월별 하루 기록 조회")
    public ApiResponse<MonthlyRecordResponse> getMonthlyRecords(
            @RequestParam int year,
            @RequestParam int month
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(recordService.getMonthlyRecords(userId, year, month));
    }

    @PatchMapping("/{recordDate}")
    @Operation(summary = "감정 점수 수정")
    public ApiResponse<RecordResponse> updateMoodScore(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate,
            @Valid @RequestBody RecordUpdateRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(recordService.updateMoodScore(userId, recordDate, request));
    }

    @GetMapping("/detail/{recordId}")
    @Operation(summary = "하루 기록 상세 조회")
    public ApiResponse<RecordDetailResponse> getRecordDetail(
            @PathVariable Long recordId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(recordDetailService.getRecordDetail(userId, recordId));
    }
}