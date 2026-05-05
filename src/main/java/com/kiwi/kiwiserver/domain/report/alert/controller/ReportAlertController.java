package com.kiwi.kiwiserver.domain.report.alert.controller;

import com.kiwi.kiwiserver.domain.report.alert.dto.response.ReportAlertResponse;
import com.kiwi.kiwiserver.domain.report.alert.dto.response.ReportAlertUnreadCountResponse;
import com.kiwi.kiwiserver.domain.report.alert.service.ReportAlertService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report-alerts")
@Tag(name = "Report Alert", description = "리포트 알림 API")
public class ReportAlertController {

    private final ReportAlertService reportAlertService;

    @GetMapping
    @Operation(summary = "리포트 알림 목록 조회")
    public ApiResponse<Page<ReportAlertResponse>> getReportAlerts(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(reportAlertService.getReportAlerts(userId, pageable));
    }

    @GetMapping("/unread-count")
    @Operation(summary = "읽지 않은 리포트 알림 개수 조회")
    public ApiResponse<ReportAlertUnreadCountResponse> getUnreadCount() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(reportAlertService.getUnreadCount(userId));
    }

    @PatchMapping("/{reportAlertId}/read")
    @Operation(summary = "리포트 알림 읽음 처리")
    public ApiResponse<Void> markAsRead(
            @PathVariable Long reportAlertId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        reportAlertService.markAsRead(userId, reportAlertId);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{reportAlertId}")
    @Operation(summary = "리포트 알림 삭제")
    public ApiResponse<Void> deleteAlert(
            @PathVariable Long reportAlertId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        reportAlertService.deleteAlert(userId, reportAlertId);
        return ApiResponse.success(null);
    }
}