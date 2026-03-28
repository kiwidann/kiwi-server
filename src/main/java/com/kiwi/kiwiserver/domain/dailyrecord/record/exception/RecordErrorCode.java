package com.kiwi.kiwiserver.domain.dailyrecord.record.exception;

import com.kiwi.kiwiserver.global.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RecordErrorCode implements BaseErrorCode {

    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "RECORD_404", "기록을 찾을 수 없습니다"),
    RECORD_ALREADY_EXISTS(HttpStatus.CONFLICT, "RECORD_409", "해당 날짜의 기록이 이미 존재합니다"),
    INVALID_RECORD_DATE(HttpStatus.BAD_REQUEST, "RECORD_400", "허용되지 않는 기록 날짜입니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    RecordErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
