package com.kiwi.kiwiserver.domain.identity.account.exception;

import com.kiwi.kiwiserver.global.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AccountErrorCode implements BaseErrorCode {

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT_404", "계정을 찾을 수 없습니다"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "ACCOUNT_409", "이미 사용 중인 이메일입니다"),
    DELETED_ACCOUNT(HttpStatus.BAD_REQUEST, "ACCOUNT_400", "탈퇴한 계정입니다"),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "ACCOUNT_401", "이메일 또는 비밀번호가 올바르지 않습니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "ACCOUNT_400", "비밀번호가 올바르지 않습니다"),
    ALREADY_DELETED_ACCOUNT(HttpStatus.BAD_REQUEST, "ACCOUNT_400", "이미 탈퇴한 계정입니다"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "ACCOUNT_401", "유효하지 않은 리프레시 토큰입니다"),
    PASSWORD_SAME_AS_OLD(HttpStatus.BAD_REQUEST, "ACCOUNT_400", "새 비밀번호는 현재 비밀번호와 달라야 합니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AccountErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}