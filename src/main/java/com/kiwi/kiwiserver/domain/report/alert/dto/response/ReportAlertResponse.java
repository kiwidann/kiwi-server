package com.kiwi.kiwiserver.domain.report.alert.dto.response;

import com.kiwi.kiwiserver.domain.report.alert.entity.ReportAlert;
import com.kiwi.kiwiserver.domain.report.alert.entity.ReportAlertType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
public class ReportAlertResponse {

    private Long reportAlertId;
    private ReportAlertType type;
    private String title;
    private String message;
    private LocalDate relatedStartDate;
    private LocalDate relatedEndDate;
    private boolean isRead;
    private Instant readAt;
    private Instant createdAt;

    public static ReportAlertResponse from(ReportAlert reportAlert) {
        return ReportAlertResponse.builder()
                .reportAlertId(reportAlert.getReportAlertId())
                .type(reportAlert.getType())
                .title(reportAlert.getTitle())
                .message(reportAlert.getMessage())
                .relatedStartDate(reportAlert.getRelatedStartDate())
                .relatedEndDate(reportAlert.getRelatedEndDate())
                .isRead(reportAlert.isRead())
                .readAt(reportAlert.getReadAt())
                .createdAt(reportAlert.getCreatedAt())
                .build();
    }
}