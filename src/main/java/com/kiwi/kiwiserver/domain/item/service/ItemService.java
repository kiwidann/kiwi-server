package com.kiwi.kiwiserver.domain.item.service;

import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.domain.identity.user.exception.UserErrorCode;
import com.kiwi.kiwiserver.domain.identity.user.repository.UserRepository;
import com.kiwi.kiwiserver.domain.item.dto.response.ItemResponse;
import com.kiwi.kiwiserver.domain.item.dto.response.PurchaseItemResponse;
import com.kiwi.kiwiserver.domain.item.entity.Item;
import com.kiwi.kiwiserver.domain.item.exception.ItemErrorCode;
import com.kiwi.kiwiserver.domain.item.mapper.ItemMapper;
import com.kiwi.kiwiserver.domain.item.repository.ItemCategoryRepository;
import com.kiwi.kiwiserver.domain.item.repository.ItemRepository;
import com.kiwi.kiwiserver.domain.item.repository.UserEquippedItemRepository;
import com.kiwi.kiwiserver.domain.item.repository.UserItemRepository;
import com.kiwi.kiwiserver.domain.kiwitransaction.entity.KiwiTransaction;
import com.kiwi.kiwiserver.domain.kiwitransaction.entity.KiwiTxType;
import com.kiwi.kiwiserver.domain.kiwitransaction.repository.KiwiTransactionRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final UserItemRepository userItemRepository;
    private final UserEquippedItemRepository userEquippedItemRepository;
    private final UserRepository userRepository;
    private final KiwiTransactionRepository kiwiTransactionRepository;
    private final ItemMapper itemMapper;

    @Transactional(readOnly = true)
    public Page<ItemResponse> getItems(Long userId, Long categoryId, boolean excludeOwned, Pageable pageable) {
        Set<Long> ownedItemIds = userItemRepository.findAllByUser_UserIdAndIsOwnedTrue(userId)
                .stream()
                .map(userItem -> userItem.getItem().getItemId())
                .collect(Collectors.toSet());

        Set<Long> equippedItemIds = userEquippedItemRepository.findAllByUser_UserId(userId)
                .stream()
                .map(userEquippedItem -> userEquippedItem.getItem().getItemId())
                .collect(Collectors.toSet());

        Page<Item> items;

        if (excludeOwned && !ownedItemIds.isEmpty()) {
            List<Long> ownedItemIdList = new ArrayList<>(ownedItemIds);

            if (categoryId == null) {
                items = itemRepository.findAllByIsActiveTrueAndItemIdNotIn(ownedItemIdList, pageable);
            } else {
                validateCategoryExists(categoryId);
                items = itemRepository.findAllByItemCategory_ItemCategoryIdAndIsActiveTrueAndItemIdNotIn(
                        categoryId, ownedItemIdList, pageable
                );
            }
        } else {
            if (categoryId == null) {
                items = itemRepository.findAllByIsActiveTrue(pageable);
            } else {
                validateCategoryExists(categoryId);
                items = itemRepository.findAllByItemCategory_ItemCategoryIdAndIsActiveTrue(categoryId, pageable);
            }
        }

        return items.map(item -> itemMapper.toItemResponse(
                item,
                ownedItemIds.contains(item.getItemId()),
                equippedItemIds.contains(item.getItemId())
        ));
    }

    public PurchaseItemResponse purchaseItem(Long userId, Long itemId) {
        User user = getUser(userId);
        Item item = getActiveItem(itemId);

        validateNotAlreadyOwned(userId, itemId);
        decreaseKiwiBalance(user, item.getPrice());

        userItemRepository.save(com.kiwi.kiwiserver.domain.item.entity.UserItem.create(user, item));

        kiwiTransactionRepository.save(
                KiwiTransaction.create(user, -item.getPrice(), KiwiTxType.PURCHASE_ITEM)
        );

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

    private void decreaseKiwiBalance(User user, int amount) {
        if (user.getKiwiBalance() < amount) {
            throw new BusinessException(ItemErrorCode.INSUFFICIENT_KIWI_BALANCE);
        }

        user.updateKiwiBalance(user.getKiwiBalance() - amount);
    }
}