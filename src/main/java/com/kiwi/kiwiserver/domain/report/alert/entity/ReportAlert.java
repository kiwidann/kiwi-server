package com.kiwi.kiwiserver.domain.report.alert.entity;

import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "report_alerts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportAlert extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_alert_id")
    private Long reportAlertId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ReportAlertType type;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "message", nullable = false, length = 500)
    private String message;

    @Column(name = "related_start_date")
    private LocalDate relatedStartDate;

    @Column(name = "related_end_date")
    private LocalDate relatedEndDate;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Column(name = "read_at")
    private Instant readAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Builder
    private ReportAlert(
            User user,
            ReportAlertType type,
            String title,
            String message,
            LocalDate relatedStartDate,
            LocalDate relatedEndDate,
            boolean isRead,
            Instant readAt,
            boolean isDeleted,
            Instant deletedAt
    ) {
        this.user = user;
        this.type = type;
        this.title = title;
        this.message = message;
        this.relatedStartDate = relatedStartDate;
        this.relatedEndDate = relatedEndDate;
        this.isRead = isRead;
        this.readAt = readAt;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }

    public static ReportAlert create(
            User user,
            ReportAlertType type,
            String title,
            String message,
            LocalDate relatedStartDate,
            LocalDate relatedEndDate
    ) {
        return ReportAlert.builder()
                .user(user)
                .type(type)
                .title(title)
                .message(message)
                .relatedStartDate(relatedStartDate)
                .relatedEndDate(relatedEndDate)
                .isRead(false)
                .isDeleted(false)
                .build();
    }

    public void markAsRead() {
        if (!this.isRead) {
            this.isRead = true;
            this.readAt = Instant.now();
        }
    }

    public void softDelete() {
        if (!this.isDeleted) {
            this.isDeleted = true;
            this.deletedAt = Instant.now();
        }
    }
}