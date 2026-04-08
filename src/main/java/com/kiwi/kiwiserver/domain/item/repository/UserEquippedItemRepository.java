package com.kiwi.kiwiserver.domain.item.repository;

import com.kiwi.kiwiserver.domain.item.entity.UserEquippedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserEquippedItemRepository extends JpaRepository<UserEquippedItem, Long> {

    // 특정 카테고리에 장착된 아이템 조회 (1개만 존재)
    Optional<UserEquippedItem> findByUser_UserIdAndItemCategory_ItemCategoryId(Long userId, Long categoryId);

    // 유저의 전체 장착 아이템 조회
    List<UserEquippedItem> findAllByUser_UserIdOrderByItemCategory_ItemCategoryIdAsc(Long userId);
}