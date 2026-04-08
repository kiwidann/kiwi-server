package com.kiwi.kiwiserver.domain.item.service;

import com.kiwi.kiwiserver.domain.item.dto.response.EquippedItemResponse;
import com.kiwi.kiwiserver.domain.item.dto.response.ItemCategoryResponse;
import com.kiwi.kiwiserver.domain.item.dto.response.ItemResponse;
import com.kiwi.kiwiserver.domain.item.dto.response.OwnedItemResponse;
import com.kiwi.kiwiserver.domain.item.entity.Item;
import com.kiwi.kiwiserver.domain.item.entity.ItemCategory;
import com.kiwi.kiwiserver.domain.item.entity.UserEquippedItem;
import com.kiwi.kiwiserver.domain.item.entity.UserItem;
import com.kiwi.kiwiserver.domain.item.exception.ItemErrorCode;
import com.kiwi.kiwiserver.domain.item.mapper.ItemMapper;
import com.kiwi.kiwiserver.domain.item.repository.ItemCategoryRepository;
import com.kiwi.kiwiserver.domain.item.repository.ItemRepository;
import com.kiwi.kiwiserver.domain.item.repository.UserEquippedItemRepository;
import com.kiwi.kiwiserver.domain.item.repository.UserItemRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemQueryServiceImpl {

    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;
    private final UserEquippedItemRepository userEquippedItemRepository;
    private final ItemMapper itemMapper;

    @Override
    public List<ItemCategoryResponse> getItemCategories() {
        List<ItemCategory> categories = itemCategoryRepository.findAllByOrderByItemCategoryIdAsc();

        return categories.stream()
                .map(itemMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public List<ItemResponse> getItems(Long categoryId) {
        List<Item> items;

        if (categoryId == null) {
            items = itemRepository.findAllByIsActiveTrueOrderByItemIdAsc();
        } else {
            validateCategoryExists(categoryId);
            items = itemRepository.findAllByItemCategory_ItemCategoryIdAndIsActiveTrueOrderByItemIdAsc(categoryId);
        }

        return items.stream()
                .map(itemMapper::toItemResponse)
                .toList();
    }

    @Override
    public List<OwnedItemResponse> getOwnedItems(Long userId) {
        List<UserItem> userItems = userItemRepository.findAllByUser_UserIdAndIsOwnedTrueOrderByUserItemIdAsc(userId);

        return userItems.stream()
                .map(itemMapper::toOwnedItemResponse)
                .toList();
    }

    @Override
    public List<EquippedItemResponse> getEquippedItems(Long userId) {
        List<UserEquippedItem> equippedItems =
                userEquippedItemRepository.findAllByUser_UserIdOrderByItemCategory_ItemCategoryIdAsc(userId);

        return equippedItems.stream()
                .map(itemMapper::toEquippedItemResponse)
                .toList();
    }

    private void validateCategoryExists(Long categoryId) {
        if (!itemCategoryRepository.existsById(categoryId)) {
            throw new BusinessException(ItemErrorCode.ITEM_CATEGORY_NOT_FOUND);
        }
    }
}