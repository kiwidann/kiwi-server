package com.kiwi.kiwiserver.domain.item.repository;

import com.kiwi.kiwiserver.domain.item.entity.UserItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    boolean existsByUser_UserIdAndItem_ItemIdAndIsOwnedTrue(Long userId, Long itemId);

    List<UserItem> findAllByUser_UserIdAndIsOwnedTrue(Long userId);

    Page<UserItem> findAllByUser_UserIdAndIsOwnedTrue(Long userId, Pageable pageable);

    Page<UserItem> findAllByUser_UserIdAndItem_ItemCategory_ItemCategoryIdAndIsOwnedTrue(
            Long userId, Long categoryId, Pageable pageable
    );
}