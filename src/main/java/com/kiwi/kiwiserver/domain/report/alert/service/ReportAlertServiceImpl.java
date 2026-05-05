package com.kiwi.kiwiserver.domain.report.alert.service;

import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.domain.identity.user.exception.UserErrorCode;
import com.kiwi.kiwiserver.domain.identity.user.repository.UserRepository;
import com.kiwi.kiwiserver.domain.report.alert.dto.response.ReportAlertResponse;
import com.kiwi.kiwiserver.domain.report.alert.dto.response.ReportAlertUnreadCountResponse;
import com.kiwi.kiwiserver.domain.report.alert.entity.ReportAlert;
import com.kiwi.kiwiserver.domain.report.alert.entity.ReportAlertType;
import com.kiwi.kiwiserver.domain.report.alert.exception.ReportAlertErrorCode;
import com.kiwi.kiwiserver.domain.report.alert.repository.ReportAlertRepository;
import com.kiwi.kiwiserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportAlertServiceImpl implements ReportAlertService {

    private final ReportAlertRepository reportAlertRepository;
    private final UserRepository userRepository;

    @Override
    public Page<ReportAlertResponse> getReportAlerts(Long userId, Pageable pageable) {
        return reportAlertRepository.findByUser_UserIdAndIsDeletedFalse(userId, pageable)
                .map(ReportAlertResponse::from);
    }

    @Override
    public ReportAlertUnreadCountResponse getUnreadCount(Long userId) {
        long unreadCount = reportAlertRepository.countByUser_UserIdAndIsDeletedFalseAndIsReadFalse(userId);

        return ReportAlertUnreadCountResponse.builder()
                .unreadCount(unreadCount)
                .build();
    }

    @Override
    @Transactional
    public void markAsRead(Long userId, Long reportAlertId) {
        ReportAlert reportAlert = getOwnedAlert(userId, reportAlertId);
        reportAlert.markAsRead();
    }

    @Override
    @Transactional
    public void deleteAlert(Long userId, Long reportAlertId) {
        ReportAlert reportAlert = getOwnedAlert(userId, reportAlertId);
        reportAlert.softDelete();
    }

    @Override
    @Transactional
    public void createAlertIfNotExists(
            Long userId,
            ReportAlertType type,
            String title,
            String message,
            LocalDate relatedStartDate,
            LocalDate relatedEndDate
    ) {
        boolean exists = reportAlertRepository
                .existsByUser_UserIdAndTypeAndRelatedStartDateAndRelatedEndDateAndIsDeletedFalse(
                        userId,
                        type,
                        relatedStartDate,
                        relatedEndDate
                );

        if (exists) {
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        ReportAlert reportAlert = ReportAlert.create(
                user,
                type,
                title,
                message,
                relatedStartDate,
                relatedEndDate
        );

        reportAlertRepository.save(reportAlert);
    }

    private ReportAlert getOwnedAlert(Long userId, Long reportAlertId) {
        ReportAlert reportAlert = reportAlertRepository.findById(reportAlertId)
                .orElseThrow(() -> new BusinessException(ReportAlertErrorCode.REPORT_ALERT_NOT_FOUND));

        if (!reportAlert.getUser().getUserId().equals(userId) || reportAlert.isDeleted()) {
            throw new BusinessException(ReportAlertErrorCode.REPORT_ALERT_FORBIDDEN);
        }

        return reportAlert;
    }
}