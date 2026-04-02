package com.kiwi.kiwiserver.domain.dailyrecord.keyword.exception;

import com.kiwi.kiwiserver.global.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum KeywordErrorCode implements BaseErrorCode {

    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "KEYWORD_404", "키워드를 찾을 수 없습니다"),
    KEYWORD_ALREADY_EXISTS(HttpStatus.CONFLICT, "KEYWORD_409", "이미 존재하는 키워드입니다"),
    INVALID_KEYWORD_REQUEST(HttpStatus.BAD_REQUEST, "KEYWORD_400", "유효하지 않은 키워드 요청입니다"),
    RECORD_NOT_OWNED(HttpStatus.FORBIDDEN, "KEYWORD_403", "해당 기록에 접근할 수 없습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    KeywordErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}