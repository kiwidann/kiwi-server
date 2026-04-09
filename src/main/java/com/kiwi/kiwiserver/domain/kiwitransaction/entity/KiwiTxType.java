package com.kiwi.kiwiserver.domain.kiwitransaction.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "키위 거래 타입")
public enum KiwiTxType {

    @Schema(description = "일기 작성 보상")
    EARN_DIARY,

    @Schema(description = "CBT 완료 보상")
    EARN_CBT,

    @Schema(description = "아이템 구매")
    PURCHASE_ITEM,

    @Schema(description = "아이템 환불")
    REFUND_ITEM,

    @Schema(description = "관리자 수동 조정")
    ADMIN_ADJUST
}