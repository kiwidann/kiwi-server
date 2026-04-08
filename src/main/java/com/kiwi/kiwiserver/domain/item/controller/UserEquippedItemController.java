package com.kiwi.kiwiserver.domain.item.controller;

import com.kiwi.kiwiserver.domain.item.dto.request.EquipItemRequest;
import com.kiwi.kiwiserver.domain.item.dto.response.EquippedItemResponse;
import com.kiwi.kiwiserver.domain.item.service.UserEquippedItemService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/equipped-items")
public class UserEquippedItemController {

    private final UserEquippedItemService userEquippedItemService;

    @Operation(summary = "내 장착 아이템 목록 조회")
    @GetMapping
    public ApiResponse<List<EquippedItemResponse>> getEquippedItems() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userEquippedItemService.getEquippedItems(userId));
    }

    @Operation(summary = "아이템 장착")
    @PutMapping("/{categoryId}")
    public ApiResponse<Void> equipItem(
            @PathVariable Long categoryId,
            @Valid @RequestBody EquipItemRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        userEquippedItemService.equipItem(userId, categoryId, request.itemId());
        return ApiResponse.success();
    }
}