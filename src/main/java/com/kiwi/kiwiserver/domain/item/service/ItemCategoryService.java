package com.kiwi.kiwiserver.domain.item.service;

import com.kiwi.kiwiserver.domain.item.dto.response.ItemCategoryResponse;
import com.kiwi.kiwiserver.domain.item.mapper.ItemMapper;
import com.kiwi.kiwiserver.domain.item.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemMapper itemMapper;

    public List<ItemCategoryResponse> getItemCategories() {
        return itemCategoryRepository.findAllByOrderByItemCategoryIdAsc()
                .stream()
                .map(itemMapper::toCategoryResponse)
                .toList();
    }
}