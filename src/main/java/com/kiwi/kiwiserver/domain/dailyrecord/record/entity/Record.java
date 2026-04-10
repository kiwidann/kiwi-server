package com.kiwi.kiwiserver.domain.dailyrecord.record.entity;

import com.kiwi.kiwiserver.domain.identity.user.entity.User;
import com.kiwi.kiwiserver.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "records",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_user_date", columnNames = {"user_id", "record_date"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Record extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_records_user"))
    private User user;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "mood_score", nullable = false)
    private Integer moodScore;

    @Builder
    private Record(User user, LocalDate recordDate, Integer moodScore) {
        validateMoodScore(moodScore);
        this.user = user;
        this.recordDate = recordDate;
        this.moodScore = moodScore;
    }

    public void updateMoodScore(Integer moodScore) {
        validateMoodScore(moodScore);
        this.moodScore = moodScore;
    }

    private void validateMoodScore(Integer moodScore) {
        if (moodScore == null || moodScore < 1 || moodScore > 10) {
            throw new IllegalArgumentException("moodScore must be between 1 and 10");
        }
    }
}