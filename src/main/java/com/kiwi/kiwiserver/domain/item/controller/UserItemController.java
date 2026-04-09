package com.kiwi.kiwiserver.domain.item.controller;

import com.kiwi.kiwiserver.domain.item.dto.response.OwnedItemResponse;
import com.kiwi.kiwiserver.domain.item.service.UserItemService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import com.kiwi.kiwiserver.global.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/users/me/items")
public class UserItemController {

    private final UserItemService userItemService;

    @Operation(summary = "내 보유 아이템 목록 조회")
    @GetMapping
    public ApiResponse<Page<OwnedItemResponse>> getOwnedItems(
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(sort = "acquiredAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ApiResponse.success(userItemService.getOwnedItems(userId, categoryId, pageable));
    }
}