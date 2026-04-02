package com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity;

import com.kiwi.kiwiserver.domain.dailyrecord.record.entity.Record;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "cbt_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CbtSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cbt_session_id")
    private Long cbtSessionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "record_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cbt_sessions_record")
    )
    private Record record;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "tag_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cbt_sessions_tag")
    )
    private Tag tag;

    @Column(name = "before_emotion_score")
    private Integer beforeEmotionScore;

    @Column(name = "after_emotion_score")
    private Integer afterEmotionScore;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Builder
    private CbtSession(Record record, Tag tag, Integer beforeEmotionScore, Integer afterEmotionScore) {
        this.record = record;
        this.tag = tag;
        this.beforeEmotionScore = beforeEmotionScore;
        this.afterEmotionScore = afterEmotionScore;
    }
}