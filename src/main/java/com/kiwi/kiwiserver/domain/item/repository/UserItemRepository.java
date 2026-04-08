package com.kiwi.kiwiserver.domain.item.repository;

import com.kiwi.kiwiserver.domain.item.entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    // 특정 유저가 특정 아이템 보유 중인지 확인
    boolean existsByUser_UserIdAndItem_ItemIdAndIsOwnedTrue(Long userId, Long itemId);

    // 특정 유저의 보유 아이템 목록 조회
    List<UserItem> findAllByUser_UserIdAndIsOwnedTrueOrderByUserItemIdAsc(Long userId);
}