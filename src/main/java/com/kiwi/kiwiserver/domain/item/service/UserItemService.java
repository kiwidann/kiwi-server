package com.kiwi.kiwiserver.domain.item.service;

import com.kiwi.kiwiserver.domain.item.dto.response.OwnedItemResponse;
import com.kiwi.kiwiserver.domain.item.mapper.ItemMapper;
import com.kiwi.kiwiserver.domain.item.repository.UserItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserItemService {

    private final UserItemRepository userItemRepository;
    private final ItemMapper itemMapper;

    public List<OwnedItemResponse> getOwnedItems(Long userId) {
        return userItemRepository.findAllByUser_UserIdAndIsOwnedTrueOrderByUserItemIdAsc(userId)
                .stream()
                .map(itemMapper::toOwnedItemResponse)
                .toList();
    }
}