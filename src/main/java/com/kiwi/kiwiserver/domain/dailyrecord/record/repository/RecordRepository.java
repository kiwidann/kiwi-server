package com.kiwi.kiwiserver.domain.dailyrecord.record.repository;

import com.kiwi.kiwiserver.domain.dailyrecord.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    boolean existsByUser_UserIdAndRecordDate(Long userId, LocalDate recordDate);

    Optional<Record> findByUser_UserIdAndRecordDate(Long userId, LocalDate recordDate);

    List<Record> findAllByUser_UserIdAndRecordDateBetweenOrderByRecordDateAsc(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}