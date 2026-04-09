package com.kiwi.kiwiserver.domain.item.service;

import com.kiwi.kiwiserver.domain.item.dto.response.OwnedItemResponse;
import com.kiwi.kiwiserver.domain.item.entity.UserItem;
import com.kiwi.kiwiserver.domain.item.exception.ItemErrorCode;
import com.kiwi.kiwiserver.domain.item.mapper.ItemMapper;
import com.kiwi.kiwiserver.domain.item.repository.ItemCategoryRepository;
import com.kiwi.kiwiserver.domain.item.repository.UserEquippedItemRepository;
import com.kiwi.kiwiserver.domain.item.repository.UserItemRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserItemService {

    private final UserItemRepository userItemRepository;
    private final UserEquippedItemRepository userEquippedItemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemMapper itemMapper;

    public Page<OwnedItemResponse> getOwnedItems(Long userId, Long categoryId, Pageable pageable) {
        Page<UserItem> userItems;

        if (categoryId == null) {
            userItems = userItemRepository.findAllByUser_UserIdAndIsOwnedTrue(userId, pageable);
        } else {
            validateCategoryExists(categoryId);
            userItems = userItemRepository
                    .findAllByUser_UserIdAndItem_ItemCategory_ItemCategoryIdAndIsOwnedTrue(userId, categoryId, pageable);
        }

        Set<Long> equippedItemIds = userEquippedItemRepository.findAllByUser_UserId(userId)
                .stream()
                .map(userEquippedItem -> userEquippedItem.getItem().getItemId())
                .collect(Collectors.toSet());

        return userItems.map(userItem -> itemMapper.toOwnedItemResponse(
                userItem,
                equippedItemIds.contains(userItem.getItem().getItemId())
        ));
    }

    private void validateCategoryExists(Long categoryId) {
        if (!itemCategoryRepository.existsById(categoryId)) {
            throw new BusinessException(ItemErrorCode.ITEM_CATEGORY_NOT_FOUND);
        }
    }
}