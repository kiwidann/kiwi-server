package com.kiwi.kiwiserver.domain.dailyrecord.thinkingtool.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "cbt_questions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_cbt_questions_tool_code_code",
                        columnNames = {"tool_code", "code"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThinkingToolQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tool_code", nullable = false, length = 50)
    private ThinkingToolCode toolCode;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "input_type", nullable = false, length = 30)
    private QuestionInputType inputType;

    @Column(name = "is_required", nullable = false)
    private boolean isRequired;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}