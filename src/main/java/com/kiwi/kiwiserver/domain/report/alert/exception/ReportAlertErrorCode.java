package com.kiwi.kiwiserver.domain.report.alert.exception;

import com.kiwi.kiwiserver.global.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReportAlertErrorCode implements BaseErrorCode {

    REPORT_ALERT_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "REPORT_ALERT_NOT_FOUND",
            "리포트 알림을 찾을 수 없습니다"
    ),

    REPORT_ALERT_FORBIDDEN(
            HttpStatus.FORBIDDEN,
            "REPORT_ALERT_FORBIDDEN",
            "해당 리포트 알림에 접근할 수 없습니다"
    ),

    REPORT_ALERT_ALREADY_READ(
            HttpStatus.BAD_REQUEST,
            "REPORT_ALERT_ALREADY_READ",
            "이미 읽은 리포트 알림입니다"
    ),

    REPORT_ALERT_ALREADY_DELETED(
            HttpStatus.BAD_REQUEST,
            "REPORT_ALERT_ALREADY_DELETED",
            "이미 삭제된 리포트 알림입니다"
    );

    private final HttpStatus status;
    private final String code;
    private final String message;

    ReportAlertErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}