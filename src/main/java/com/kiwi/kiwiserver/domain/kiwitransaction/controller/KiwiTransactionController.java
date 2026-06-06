package com.kiwi.kiwiserver.domain.kiwitransaction.controller;

import com.kiwi.kiwiserver.domain.kiwitransaction.dto.response.KiwiTransactionResponse;
import com.kiwi.kiwiserver.domain.kiwitransaction.entity.KiwiTxType;
import com.kiwi.kiwiserver.domain.kiwitransaction.service.KiwiTransactionService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/kiwi-transactions")
public class KiwiTransactionController {

    private final KiwiTransactionService kiwiTransactionService;

    @Operation(summary = "내 키위 거래 내역 조회")
    @GetMapping
    public ApiResponse<Page<KiwiTransactionResponse>> getKiwiTransactions(
            @Parameter(
                    description = "거래 타입 필터",
                    example = "PURCHASE_ITEM"
            )
            @RequestParam(required = false) KiwiTxType type,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(
                kiwiTransactionService.getKiwiTransactions(userId, type, pageable)
        );
    }
}