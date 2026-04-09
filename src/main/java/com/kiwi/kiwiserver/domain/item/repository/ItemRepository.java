package com.kiwi.kiwiserver.domain.item.repository;

import com.kiwi.kiwiserver.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByIsActiveTrue(Pageable pageable);

    Page<Item> findAllByItemCategory_ItemCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);
}