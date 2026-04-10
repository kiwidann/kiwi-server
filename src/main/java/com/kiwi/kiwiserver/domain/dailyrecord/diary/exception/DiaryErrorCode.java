package com.kiwi.kiwiserver.domain.dailyrecord.diary.exception;

import com.kiwi.kiwiserver.global.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DiaryErrorCode implements BaseErrorCode {

    DIARY_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "DIARY_NOT_FOUND",
            "일기를 찾을 수 없습니다"
    ),

    DIARY_RECORD_NOT_OWNED(
            HttpStatus.FORBIDDEN,
            "DIARY_RECORD_NOT_OWNED",
            "해당 기록에 접근할 수 없습니다"
    ),

    DIARY_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "DIARY_ALREADY_EXISTS",
            "해당 기록의 일기는 이미 존재합니다"
    );

    private final HttpStatus status;
    private final String code;
    private final String message;

    DiaryErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}