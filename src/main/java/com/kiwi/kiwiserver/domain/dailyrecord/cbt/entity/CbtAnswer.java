package com.kiwi.kiwiserver.domain.dailyrecord.cbt.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "cbt_answers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CbtAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "cbt_session_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cbt_answers_session")
    )
    private CbtSession cbtSession;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "question_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cbt_answers_question")
    )
    private CbtQuestion question;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    public CbtAnswer(CbtSession cbtSession, CbtQuestion question, String answerText) {
        this.cbtSession = cbtSession;
        this.question = question;
        this.answerText = answerText;
    }
}