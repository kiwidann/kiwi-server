package com.kiwi.kiwiserver.domain.item.exception;

import com.kiwi.kiwiserver.global.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ItemErrorCode implements BaseErrorCode {

    ITEM_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "ITEM_NOT_FOUND",
            "아이템을 찾을 수 없습니다"
    ),

    ITEM_CATEGORY_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "ITEM_CATEGORY_NOT_FOUND",
            "아이템 카테고리를 찾을 수 없습니다"
    ),

    ITEM_NOT_ACTIVE(
            HttpStatus.BAD_REQUEST,
            "ITEM_NOT_ACTIVE",
            "비활성화된 아이템입니다"
    ),

    ITEM_ALREADY_OWNED(
            HttpStatus.BAD_REQUEST,
            "ITEM_ALREADY_OWNED",
            "이미 보유 중인 아이템입니다"
    ),

    ITEM_NOT_OWNED(
            HttpStatus.BAD_REQUEST,
            "ITEM_NOT_OWNED",
            "보유하지 않은 아이템입니다"
    ),

    ITEM_CATEGORY_MISMATCH(
            HttpStatus.BAD_REQUEST,
            "ITEM_CATEGORY_MISMATCH",
            "아이템 카테고리가 일치하지 않습니다"
    ),

    ITEM_INSUFFICIENT_KIWI_BALANCE(
            HttpStatus.BAD_REQUEST,
            "ITEM_INSUFFICIENT_KIWI_BALANCE",
            "키위가 부족합니다"
    );

    private final HttpStatus status;
    private final String code;
    private final String message;

    ItemErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}