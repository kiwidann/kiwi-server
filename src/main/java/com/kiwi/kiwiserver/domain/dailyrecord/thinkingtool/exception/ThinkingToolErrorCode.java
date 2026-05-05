package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.exception;

import com.kiwi.kiwiserver.global.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ThinkingToolErrorCode implements BaseErrorCode {

    THINKING_TOOL_SESSION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "THINKING_TOOL_NOT_FOUND",
            "생각정리도구 세션을 찾을 수 없습니다"
    ),

    THINKING_TOOL_TAG_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "THINKING_TOOL_TAG_NOT_FOUND",
            "생각정리도구 태그를 찾을 수 없습니다"
    ),

    THINKING_TOOL_QUESTION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "THINKING_TOOL_QUESTION_NOT_FOUND",
            "생각정리도구 질문을 찾을 수 없습니다"
    ),

    THINKING_TOOL_REQUIRED_ANSWER_MISSING(
            HttpStatus.BAD_REQUEST,
            "THINKING_TOOL_REQUIRED_ANSWER_MISSING",
            "필수 질문 답변이 누락되었습니다"
    ),

    THINKING_TOOL_DUPLICATE_QUESTION(
            HttpStatus.BAD_REQUEST,
            "THINKING_TOOL_DUPLICATE_QUESTION",
            "중복된 질문이 포함되어 있습니다"
    ),

    THINKING_TOOL_INVALID_REQUEST(
            HttpStatus.BAD_REQUEST,
            "THINKING_TOOL_INVALID_REQUEST",
            "잘못된 생각정리도구 요청입니다"
    ),

    THINKING_TOOL_RECORD_NOT_OWNED(
            HttpStatus.FORBIDDEN,
            "THINKING_TOOL_RECORD_NOT_OWNED",
            "해당 기록에 접근할 수 없습니다"
    );

    private final HttpStatus status;
    private final String code;
    private final String message;

    ThinkingToolErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}