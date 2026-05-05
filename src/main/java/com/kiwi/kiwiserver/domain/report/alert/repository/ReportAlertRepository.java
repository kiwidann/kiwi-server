package com.kiwi.kiwiserver.domain.report.alert.repository;

import com.kiwi.kiwiserver.domain.report.alert.entity.ReportAlert;
import com.kiwi.kiwiserver.domain.report.alert.entity.ReportAlertType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ReportAlertRepository extends JpaRepository<ReportAlert, Long> {

    Page<ReportAlert> findByUser_UserIdAndIsDeletedFalse(Long userId, Pageable pageable);

    long countByUser_UserIdAndIsDeletedFalseAndIsReadFalse(Long userId);

    boolean existsByUser_UserIdAndTypeAndRelatedStartDateAndRelatedEndDateAndIsDeletedFalse(
            Long userId,
            ReportAlertType type,
            LocalDate relatedStartDate,
            LocalDate relatedEndDate
    );
}