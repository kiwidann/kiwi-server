package com.kiwi.kiwiserver.domain.report.alert.service;

import com.kiwi.kiwiserver.domain.report.alert.dto.response.ReportAlertResponse;
import com.kiwi.kiwiserver.domain.report.alert.dto.response.ReportAlertUnreadCountResponse;
import com.kiwi.kiwiserver.domain.report.alert.entity.ReportAlertType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ReportAlertService {

    Page<ReportAlertResponse> getReportAlerts(Long userId, Pageable pageable);

    ReportAlertUnreadCountResponse getUnreadCount(Long userId);

    void markAsRead(Long userId, Long reportAlertId);

    void deleteAlert(Long userId, Long reportAlertId);

    void createAlertIfNotExists(
            Long userId,
            ReportAlertType type,
            String title,
            String message,
            LocalDate relatedStartDate,
            LocalDate relatedEndDate
    );
}