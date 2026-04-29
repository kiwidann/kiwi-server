package com.kiwi.kiwiserver.domain.report.report.controller;

import com.kiwi.kiwiserver.domain.report.report.dto.response.CbtReportResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.EmotionTrendResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.KeywordReportResponse;
import com.kiwi.kiwiserver.domain.report.report.dto.response.ReportDashboardResponse;
import com.kiwi.kiwiserver.domain.report.report.service.ReportService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
@Tag(name = "Report", description = "리포트 API")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard")
    @Operation(summary = "리포트 대시보드 조회")
    public ApiResponse<ReportDashboardResponse> getDashboard(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(reportService.getDashboard(userId, from, to));
    }

    @GetMapping("/emotions")
    @Operation(summary = "감정 추이 리포트 조회")
    public ApiResponse<EmotionTrendResponse> getEmotionTrend(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(reportService.getEmotionTrend(userId, from, to));
    }

    @GetMapping("/keywords")
    @Operation(summary = "키워드 리포트 조회")
    public ApiResponse<KeywordReportResponse> getKeywordReport(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(defaultValue = "5") int limit
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(reportService.getKeywordReport(userId, from, to, limit));
    }

    @GetMapping("/cbt")
    @Operation(summary = "CBT 리포트 조회")
    public ApiResponse<CbtReportResponse> getCbtReport(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(reportService.getCbtReport(userId, from, to));
    }
}