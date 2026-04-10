package com.kiwi.kiwiserver.domain.identity.account.exception;

import com.kiwi.kiwiserver.global.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AccountErrorCode implements BaseErrorCode {

    ACCOUNT_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "ACCOUNT_NOT_FOUND",
            "계정을 찾을 수 없습니다"
    ),
    ACCOUNT_EMAIL_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "ACCOUNT_EMAIL_ALREADY_EXISTS",
            "이미 사용 중인 이메일입니다"
    ),
    ACCOUNT_DELETED(
            HttpStatus.BAD_REQUEST,
            "ACCOUNT_DELETED",
            "탈퇴한 계정입니다"
    ),
    ACCOUNT_ALREADY_DELETED(
            HttpStatus.BAD_REQUEST,
            "ACCOUNT_ALREADY_DELETED",
            "이미 탈퇴한 계정입니다"
    ),
    ACCOUNT_INVALID_CREDENTIALS(
            HttpStatus.UNAUTHORIZED,
            "ACCOUNT_INVALID_CREDENTIALS",
            "이메일 또는 비밀번호가 올바르지 않습니다"
    ),
    ACCOUNT_INVALID_PASSWORD(
            HttpStatus.BAD_REQUEST,
            "ACCOUNT_INVALID_PASSWORD",
            "비밀번호가 올바르지 않습니다"
    ),
    ACCOUNT_PASSWORD_SAME_AS_OLD(
            HttpStatus.BAD_REQUEST,
            "ACCOUNT_PASSWORD_SAME_AS_OLD",
            "새 비밀번호는 현재 비밀번호와 달라야 합니다"
    ),
    ACCOUNT_INVALID_REFRESH_TOKEN(
            HttpStatus.UNAUTHORIZED,
            "ACCOUNT_INVALID_REFRESH_TOKEN",
            "유효하지 않은 리프레시 토큰입니다"
    ),
    ACCOUNT_EMAIL_NOT_VERIFIED(
            HttpStatus.UNAUTHORIZED,
            "ACCOUNT_EMAIL_NOT_VERIFIED",
            "이메일 인증이 필요합니다"
    ),
    ACCOUNT_ALREADY_VERIFIED(
            HttpStatus.BAD_REQUEST,
            "ACCOUNT_ALREADY_VERIFIED",
            "이미 인증이 완료된 계정입니다"
    ),
    ACCOUNT_VERIFICATION_CODE_NOT_FOUND(
            HttpStatus.BAD_REQUEST,
            "ACCOUNT_VERIFICATION_CODE_NOT_FOUND",
            "인증 코드가 없거나 만료되었습니다"
    ),
    ACCOUNT_INVALID_VERIFICATION_CODE(
            HttpStatus.BAD_REQUEST,
            "ACCOUNT_INVALID_VERIFICATION_CODE",
            "인증 코드가 올바르지 않습니다"
    ),
    ACCOUNT_VERIFICATION_REQUIRED_FOR_PASSWORD_RESET(
            HttpStatus.BAD_REQUEST,
            "ACCOUNT_VERIFICATION_REQUIRED_FOR_PASSWORD_RESET",
            "비밀번호 재설정 인증이 완료되지 않았습니다"
    ),
    ACCOUNT_TOO_MANY_VERIFICATION_REQUESTS(
            HttpStatus.TOO_MANY_REQUESTS,
            "ACCOUNT_TOO_MANY_VERIFICATION_REQUESTS",
            "인증 코드 재전송 요청이 너무 많습니다. 잠시 후 다시 시도해주세요"
    ),
    ACCOUNT_TOO_MANY_VERIFICATION_ATTEMPTS(
            HttpStatus.TOO_MANY_REQUESTS,
            "ACCOUNT_TOO_MANY_VERIFICATION_ATTEMPTS",
            "인증 코드 입력 시도 횟수를 초과했습니다. 새 인증 코드를 요청해주세요"
    ),
    ACCOUNT_TOO_MANY_VERIFICATION_SENDS(
            HttpStatus.TOO_MANY_REQUESTS,
            "ACCOUNT_TOO_MANY_VERIFICATION_SENDS",
            "인증 코드 발송 횟수를 초과했습니다. 잠시 후 다시 시도해주세요"
    );

    private final HttpStatus status;
    private final String code;
    private final String message;

    AccountErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}