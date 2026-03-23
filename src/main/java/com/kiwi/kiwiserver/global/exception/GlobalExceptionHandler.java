package com.kiwi.kiwiserver.global.exception;

import com.kiwi.kiwiserver.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(final BusinessException e) {
        BaseErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }

    // DTO @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            final MethodArgumentNotValidException e
    ) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("COMMON_400","요청한 값이 올바르지 않습니다"));
    }

    // JSON 형식 오류 / 타입 불일치
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException e
    ) {
      return ResponseEntity
              .badRequest()
              .body(ApiResponse.error("COMMON_400","잘못된 요청 형식입니다"));
    }

    // 최종 fallback (예상 못 한 서버 에러)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(final Exception e) {
        log.error("Unexpected exception occurred", e);

        BaseErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }
}
