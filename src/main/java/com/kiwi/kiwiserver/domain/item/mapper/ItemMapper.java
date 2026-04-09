package com.kiwi.kiwiserver.domain.item.mapper;

import com.kiwi.kiwiserver.domain.item.dto.response.EquippedItemResponse;
import com.kiwi.kiwiserver.domain.item.dto.response.ItemCategoryResponse;
import com.kiwi.kiwiserver.domain.item.dto.response.ItemResponse;
import com.kiwi.kiwiserver.domain.item.dto.response.OwnedItemResponse;
import com.kiwi.kiwiserver.domain.item.entity.Item;
import com.kiwi.kiwiserver.domain.item.entity.ItemCategory;
import com.kiwi.kiwiserver.domain.item.entity.UserEquippedItem;
import com.kiwi.kiwiserver.domain.item.entity.UserItem;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemCategoryResponse toCategoryResponse(ItemCategory itemCategory) {
        return new ItemCategoryResponse(
                itemCategory.getItemCategoryId(),
                itemCategory.getName()
        );
    }

    public ItemResponse toItemResponse(Item item, boolean owned, boolean equipped) {
        return new ItemResponse(
                item.getItemId(),
                item.getItemCategory().getItemCategoryId(),
                item.getItemCategory().getName(),
                item.getName(),
                item.getDescription(),
                item.getImageUrl(),
                item.getPrice(),
                item.isActive(),
                owned,
                equipped
        );
    }

    public OwnedItemResponse toOwnedItemResponse(UserItem userItem, boolean equipped) {
        Item item = userItem.getItem();

        return new OwnedItemResponse(
                userItem.getUserItemId(),
                item.getItemId(),
                item.getItemCategory().getItemCategoryId(),
                item.getItemCategory().getName(),
                item.getName(),
                item.getDescription(),
                item.getImageUrl(),
                item.getPrice(),
                userItem.getAcquiredAt(),
                equipped
        );
    }

    public EquippedItemResponse toEquippedItemResponse(UserEquippedItem equippedItem) {
        Item item = equippedItem.getItem();

        return new EquippedItemResponse(
                equippedItem.getUserEquippedItemId(),
                equippedItem.getItemCategory().getItemCategoryId(),
                equippedItem.getItemCategory().getName(),
                item.getItemId(),
                item.getName(),
                item.getImageUrl(),
                equippedItem.getEquippedAt()
        );
    }
}