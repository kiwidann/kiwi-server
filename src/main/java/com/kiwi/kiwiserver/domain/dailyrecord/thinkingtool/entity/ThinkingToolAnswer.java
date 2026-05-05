package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity;

import com.kiwi.kiwiserver.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cbt_answers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThinkingToolAnswer extends BaseTimeEntity {

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
    private ThinkingToolSession thinkingToolSession;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "question_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cbt_answers_question")
    )
    private ThinkingToolQuestion question;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "answer_value")
    private Integer answerValue;

    public ThinkingToolAnswer(
            ThinkingToolSession thinkingToolSession,
            ThinkingToolQuestion question,
            String answerText,
            Integer answerValue
    ) {
        this.thinkingToolSession = thinkingToolSession;
        this.question = question;
        this.answerText = answerText;
        this.answerValue = answerValue;
    }
}