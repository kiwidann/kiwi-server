package com.kiwi.kiwiserver.domain.dailyrecord.diary.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findAllByRecord_RecordIdAndIsDeletedFalseOrderByCreatedAtAsc(Long recordId);

    Optional<Diary> findByDiaryIdAndIsDeletedFalse(Long diaryId);

    Optional<Diary> findByDiaryIdAndRecord_RecordIdAndIsDeletedFalse(Long diaryId, Long recordId);
}