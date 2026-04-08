package com.kiwi.kiwiserver.domain.item.service;

import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.domain.identity.user.exception.UserErrorCode;
import com.kiwi.kiwiserver.domain.identity.user.repository.UserRepository;
import com.kiwi.kiwiserver.domain.item.dto.response.ItemResponse;
import com.kiwi.kiwiserver.domain.item.dto.response.PurchaseItemResponse;
import com.kiwi.kiwiserver.domain.item.entity.Item;
import com.kiwi.kiwiserver.domain.item.entity.UserItem;
import com.kiwi.kiwiserver.domain.item.exception.ItemErrorCode;
import com.kiwi.kiwiserver.domain.item.mapper.ItemMapper;
import com.kiwi.kiwiserver.domain.item.repository.ItemCategoryRepository;
import com.kiwi.kiwiserver.domain.item.repository.ItemRepository;
import com.kiwi.kiwiserver.domain.item.repository.UserItemRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final UserItemRepository userItemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Transactional(readOnly = true)
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

    public PurchaseItemResponse purchaseItem(Long userId, Long itemId) {
        User user = getUser(userId);
        Item item = getActiveItem(itemId);

        validateNotAlreadyOwned(userId, itemId);
        validateEnoughBalance(user, item.getPrice());

        user.decreaseKiwiBalance(item.getPrice());
        userItemRepository.save(UserItem.create(user, item));

        return new PurchaseItemResponse(
                item.getItemId(),
                item.getName(),
                item.getPrice(),
                user.getKiwiBalance()
        );
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

    private void validateCategoryExists(Long categoryId) {
        if (!itemCategoryRepository.existsById(categoryId)) {
            throw new BusinessException(ItemErrorCode.ITEM_CATEGORY_NOT_FOUND);
        }
    }

    private void validateNotAlreadyOwned(Long userId, Long itemId) {
        if (userItemRepository.existsByUser_UserIdAndItem_ItemIdAndIsOwnedTrue(userId, itemId)) {
            throw new BusinessException(ItemErrorCode.ITEM_ALREADY_OWNED);
        }
    }

    private void validateEnoughBalance(User user, int price) {
        if (user.getKiwiBalance() < price) {
            throw new BusinessException(ItemErrorCode.INSUFFICIENT_KIWI_BALANCE);
        }
    }
}