package com.kiwi.kiwiserver.domain.item.service;

import com.kiwi.kiwiserver.domain.item.dto.response.OwnedItemResponse;
import com.kiwi.kiwiserver.domain.item.exception.ItemErrorCode;
import com.kiwi.kiwiserver.domain.item.mapper.ItemMapper;
import com.kiwi.kiwiserver.domain.item.repository.ItemCategoryRepository;
import com.kiwi.kiwiserver.domain.item.repository.UserItemRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserItemService {

    private final UserItemRepository userItemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemMapper itemMapper;

    public List<OwnedItemResponse> getOwnedItems(Long userId, Long categoryId) {
        if (categoryId == null) {
            return userItemRepository.findAllByUser_UserIdAndIsOwnedTrueOrderByUserItemIdAsc(userId)
                    .stream()
                    .map(itemMapper::toOwnedItemResponse)
                    .toList();
        }

        validateCategoryExists(categoryId);

        return userItemRepository
                .findAllByUser_UserIdAndItem_ItemCategory_ItemCategoryIdAndIsOwnedTrueOrderByUserItemIdAsc(userId, categoryId)
                .stream()
                .map(itemMapper::toOwnedItemResponse)
                .toList();
    }

    private void validateCategoryExists(Long categoryId) {
        if (!itemCategoryRepository.existsById(categoryId)) {
            throw new BusinessException(ItemErrorCode.ITEM_CATEGORY_NOT_FOUND);
        }
    }
}