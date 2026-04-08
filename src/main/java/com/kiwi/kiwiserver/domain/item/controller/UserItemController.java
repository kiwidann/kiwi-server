package com.kiwi.kiwiserver.domain.item.controller;

import com.kiwi.kiwiserver.domain.item.dto.response.OwnedItemResponse;
import com.kiwi.kiwiserver.domain.item.service.UserItemService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/items")
public class UserItemController {

    private final UserItemService userItemService;

    @Operation(summary = "내 보유 아이템 목록 조회")
    @GetMapping
    public ApiResponse<List<OwnedItemResponse>> getOwnedItems() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userItemService.getOwnedItems(userId));
    }
}