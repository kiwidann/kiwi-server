package com.kiwi.kiwiserver.domain.item.service;

import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.domain.identity.user.exception.UserErrorCode;
import com.kiwi.kiwiserver.domain.identity.user.repository.UserRepository;
import com.kiwi.kiwiserver.domain.item.dto.response.EquippedItemResponse;
import com.kiwi.kiwiserver.domain.item.entity.Item;
import com.kiwi.kiwiserver.domain.item.entity.ItemCategory;
import com.kiwi.kiwiserver.domain.item.entity.UserEquippedItem;
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
@Transactional
public class UserEquippedItemService {

    private final UserEquippedItemRepository userEquippedItemRepository;
    private final UserItemRepository userItemRepository;
    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Transactional(readOnly = true)
    public List<EquippedItemResponse> getEquippedItems(Long userId) {
        return userEquippedItemRepository.findAllByUser_UserIdOrderByItemCategory_ItemCategoryIdAsc(userId)
                .stream()
                .map(itemMapper::toEquippedItemResponse)
                .toList();
    }

    public void equipItem(Long userId, Long categoryId, Long itemId) {
        User user = getUser(userId);
        Item item = getActiveItem(itemId);
        ItemCategory itemCategory = getItemCategory(categoryId);

        validateUserOwnsItem(userId, itemId);
        validateItemCategoryMatches(categoryId, item);

        UserEquippedItem equippedItem = userEquippedItemRepository
                .findByUser_UserIdAndItemCategory_ItemCategoryId(userId, categoryId)
                .orElse(null);

        if (equippedItem == null) {
            userEquippedItemRepository.save(UserEquippedItem.create(user, itemCategory, item));
            return;
        }

        equippedItem.changeItem(item);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
    }

    private Item getActiveItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ItemErrorCode.ITEM_NOT_FOUND));

        if (!item.isActive()) {
            throw new BusinessException(ItemErrorCode.ITEM_NOT_ACTIVE);
        }

        return item;
    }

    private ItemCategory getItemCategory(Long categoryId) {
        return itemCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ItemErrorCode.ITEM_CATEGORY_NOT_FOUND));
    }

    private void validateUserOwnsItem(Long userId, Long itemId) {
        if (!userItemRepository.existsByUser_UserIdAndItem_ItemIdAndIsOwnedTrue(userId, itemId)) {
            throw new BusinessException(ItemErrorCode.ITEM_NOT_OWNED);
        }
    }

    private void validateItemCategoryMatches(Long categoryId, Item item) {
        if (!item.getItemCategory().getItemCategoryId().equals(categoryId)) {
            throw new BusinessException(ItemErrorCode.ITEM_CATEGORY_MISMATCH);
        }
    }
}