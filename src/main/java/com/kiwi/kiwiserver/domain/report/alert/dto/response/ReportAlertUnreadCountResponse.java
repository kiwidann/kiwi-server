package com.kiwi.kiwiserver.domain.report.alert.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportAlertUnreadCountResponse {

    private long unreadCount;
}