package com.kiwi.kiwiserver.domain.item.repository;

import com.kiwi.kiwiserver.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 활성화된 전체 아이템 조회
    List<Item> findAllByIsActiveTrueOrderByItemIdAsc();

    // 카테고리별 활성 아이템 조회
    List<Item> findAllByItemCategory_ItemCategoryIdAndIsActiveTrueOrderByItemIdAsc(Long categoryId);
}