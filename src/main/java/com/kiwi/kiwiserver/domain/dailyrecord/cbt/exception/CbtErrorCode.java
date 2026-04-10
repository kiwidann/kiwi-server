package com.kiwi.kiwiserver.domain.dailyrecord.cbt.exception;

import com.kiwi.kiwiserver.global.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CbtErrorCode implements BaseErrorCode {

    CBT_SESSION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "CBT_SESSION_NOT_FOUND",
            "CBT 세션을 찾을 수 없습니다"
    ),

    CBT_TAG_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "CBT_TAG_NOT_FOUND",
            "CBT 태그를 찾을 수 없습니다"
    ),

    CBT_QUESTION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "CBT_QUESTION_NOT_FOUND",
            "CBT 질문을 찾을 수 없습니다"
    ),

    CBT_REQUIRED_ANSWER_MISSING(
            HttpStatus.BAD_REQUEST,
            "CBT_REQUIRED_ANSWER_MISSING",
            "필수 질문 답변이 누락되었습니다"
    ),

    CBT_DUPLICATE_QUESTION(
            HttpStatus.BAD_REQUEST,
            "CBT_DUPLICATE_QUESTION",
            "중복된 질문이 포함되어 있습니다"
    ),

    CBT_INVALID_REQUEST(
            HttpStatus.BAD_REQUEST,
            "CBT_INVALID_REQUEST",
            "잘못된 CBT 요청입니다"
    ),

    CBT_RECORD_NOT_OWNED(
            HttpStatus.FORBIDDEN,
            "CBT_RECORD_NOT_OWNED",
            "해당 기록에 접근할 수 없습니다"
    );

    private final HttpStatus status;
    private final String code;
    private final String message;

    CbtErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}