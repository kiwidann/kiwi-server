package com.kiwi.kiwiserver.domain.item.repository;

import com.kiwi.kiwiserver.domain.item.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    List<ItemCategory> findAllByOrderByItemCategoryIdAsc();
}