package com.kiwi.kiwiserver.domain.item.controller;

import com.kiwi.kiwiserver.domain.item.dto.response.ItemCategoryResponse;
import com.kiwi.kiwiserver.domain.item.service.ItemCategoryService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item-categories")
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    @Operation(summary = "아이템 카테고리 목록 조회")
    @GetMapping
    public ApiResponse<List<ItemCategoryResponse>> getItemCategories() {
        return ApiResponse.success(itemCategoryService.getItemCategories());
    }
}