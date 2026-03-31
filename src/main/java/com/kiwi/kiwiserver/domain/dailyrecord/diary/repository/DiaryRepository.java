package com.kiwi.kiwiserver.domain.dailyrecord.diary.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.diary.entity.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByRecord_RecordId(Long recordId);

    boolean existsByRecord_RecordId(Long recordId);

    Page<Diary> findAllByRecord_User_UserId(Long userId, Pageable pageable);
}