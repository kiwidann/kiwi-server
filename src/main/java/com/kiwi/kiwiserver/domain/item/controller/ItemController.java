package com.kiwi.kiwiserver.domain.item.controller;

import com.kiwi.kiwiserver.domain.item.dto.response.ItemResponse;
import com.kiwi.kiwiserver.domain.item.dto.response.PurchaseItemResponse;
import com.kiwi.kiwiserver.domain.item.service.ItemService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "아이템 목록 조회")
    @GetMapping
    public ApiResponse<List<ItemResponse>> getItems(
            @RequestParam(required = false) Long categoryId
    ) {
        return ApiResponse.success(itemService.getItems(categoryId));
    }

    @Operation(summary = "아이템 구매")
    @PostMapping("/{itemId}/purchase")
    public ApiResponse<PurchaseItemResponse> purchaseItem(
            @PathVariable Long itemId
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(itemService.purchaseItem(userId, itemId));
    }
}