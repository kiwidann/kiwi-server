package com.kiwi.kiwiserver.domain.item.repository;

import com.kiwi.kiwiserver.domain.item.entity.UserEquippedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserEquippedItemRepository extends JpaRepository<UserEquippedItem, Long> {

    Optional<UserEquippedItem> findByUser_UserIdAndItemCategory_ItemCategoryId(Long userId, Long categoryId);

    List<UserEquippedItem> findAllByUser_UserIdOrderByItemCategory_ItemCategoryIdAsc(Long userId);

    List<UserEquippedItem> findAllByUser_UserId(Long userId);
}