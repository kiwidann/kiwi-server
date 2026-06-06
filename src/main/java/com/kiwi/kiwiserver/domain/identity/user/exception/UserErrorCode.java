package com.kiwi.kiwiserver.domain.identity.user.exception;

import com.kiwi.kiwiserver.global.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "USER_NOT_FOUND",
            "사용자를 찾을 수 없습니다"
    ),

    USER_INVALID_NICKNAME(
            HttpStatus.BAD_REQUEST,
            "USER_INVALID_NICKNAME",
            "유효하지 않은 닉네임입니다"
    );

    private final HttpStatus status;
    private final String code;
    private final String message;

    UserErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}