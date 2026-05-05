-- CBT 테이블을 생각 정리 도구 구조로 확장
-- 테이블명은 유지하고 tool_code로 도구 종류를 구분한다

-- =========================
-- 1. cbt_sessions: 사용한 생각 정리 도구 코드 추가
-- =========================
ALTER TABLE cbt_sessions
    ADD COLUMN tool_code VARCHAR(50);

UPDATE cbt_sessions
SET tool_code = 'RETHINK_THOUGHT'
WHERE tool_code IS NULL;

ALTER TABLE cbt_sessions
    ALTER COLUMN tool_code SET NOT NULL;

-- =========================
-- 2. cbt_questions: 도구 코드와 입력 타입 추가
-- =========================
ALTER TABLE cbt_questions
    ADD COLUMN tool_code VARCHAR(50);

ALTER TABLE cbt_questions
    ADD COLUMN input_type VARCHAR(30);

UPDATE cbt_questions
SET tool_code = 'RETHINK_THOUGHT'
WHERE tool_code IS NULL;

UPDATE cbt_questions
SET input_type = 'TEXT'
WHERE input_type IS NULL;

ALTER TABLE cbt_questions
    ALTER COLUMN tool_code SET NOT NULL;

ALTER TABLE cbt_questions
    ALTER COLUMN input_type SET NOT NULL;

-- =========================
-- 3. cbt_answers: 슬라이더/숫자 입력값 저장 컬럼 추가
-- =========================
ALTER TABLE cbt_answers
    ADD COLUMN answer_value INT;

-- =========================
-- 4. 기존 질문 데이터 정리
-- 개발 DB 기준: 기존 CBT 답변/질문 제거 후 생각 정리 도구 질문 재삽입
-- =========================
DELETE FROM cbt_answers;
DELETE FROM cbt_questions;

-- =========================
-- 5. 기존 question code UNIQUE 제약 제거 후 복합 UNIQUE로 변경
-- 같은 code가 도구별로 반복될 수 있게 함
-- =========================
ALTER TABLE cbt_questions
DROP CONSTRAINT IF EXISTS cbt_questions_code_key;

ALTER TABLE cbt_questions
    ADD CONSTRAINT uq_cbt_questions_tool_code_code UNIQUE (tool_code, code);

-- =========================
-- 6. 입력 타입 제약 추가
-- =========================
ALTER TABLE cbt_questions
    ADD CONSTRAINT chk_cbt_questions_input_type
        CHECK (input_type IN ('TEXT', 'CHECKBOX', 'SLIDER', 'GUIDE'));

-- =========================
-- 7. 슬라이더 값 범위 제약 추가
-- =========================
ALTER TABLE cbt_answers
    ADD CONSTRAINT chk_cbt_answers_answer_value
        CHECK (answer_value IS NULL OR answer_value BETWEEN 0 AND 100);

-- =========================
-- 8. 생각 정리 도구 질문 데이터 삽입
-- =========================

-- 1. 생각 다시 보기
INSERT INTO cbt_questions (tool_code, code, question_text, display_order, input_type, is_required, is_active)
VALUES
    ('RETHINK_THOUGHT', 'AUTOMATIC_THOUGHT', '지금 떠오른 생각은 무엇인가요?', 1, 'TEXT', TRUE, TRUE),
    ('RETHINK_THOUGHT', 'EVIDENCE_FOR', '이 생각을 뒷받침하는 근거는 무엇인가요?', 2, 'TEXT', TRUE, TRUE),
    ('RETHINK_THOUGHT', 'EVIDENCE_AGAINST', '반대되는 근거는 무엇인가요?', 3, 'TEXT', TRUE, TRUE),
    ('RETHINK_THOUGHT', 'THOUGHT_CHECK', '이 생각에 가까운 것을 골라주세요', 4, 'CHECKBOX', FALSE, TRUE);

-- 2. 다른 시선에서 바라보기
INSERT INTO cbt_questions (tool_code, code, question_text, display_order, input_type, is_required, is_active)
VALUES
    ('NEW_PERSPECTIVE', 'SITUATION', '어떤 상황이 있었나요?', 1, 'TEXT', TRUE, TRUE),
    ('NEW_PERSPECTIVE', 'FRIEND_ADVICE', '친구가 같은 고민이라면 뭐라고 말해줄까요?', 2, 'TEXT', TRUE, TRUE),
    ('NEW_PERSPECTIVE', 'OTHER_INTERPRETATION', '다른 해석을 써보면 어떻게 볼 수 있나요?', 3, 'TEXT', TRUE, TRUE);

-- 3. 느낌과 사실 나누기
INSERT INTO cbt_questions (tool_code, code, question_text, display_order, input_type, is_required, is_active)
VALUES
    ('EMOTION_FACT', 'EMOTION', '지금 느끼는 감정은 무엇인가요?', 1, 'TEXT', TRUE, TRUE),
    ('EMOTION_FACT', 'FACT', '실제로 확인할 수 있는 사실은 무엇인가요?', 2, 'TEXT', TRUE, TRUE);

-- 4. 최악의 생각 살펴보기
INSERT INTO cbt_questions (tool_code, code, question_text, display_order, input_type, is_required, is_active)
VALUES
    ('WORST_THOUGHT', 'WORST_RESULT', '내가 걱정하는 최악의 결과는 무엇인가요?', 1, 'TEXT', TRUE, TRUE),
    ('WORST_THOUGHT', 'PROBABILITY', '실제로 일어날 가능성은 어느 정도인가요?', 2, 'SLIDER', TRUE, TRUE),
    ('WORST_THOUGHT', 'COPING_ABILITY', '실제 발생해도 대처 가능성은 어느 정도인가요?', 3, 'SLIDER', TRUE, TRUE);

-- 5. 마음 쉬어가기
INSERT INTO cbt_questions (tool_code, code, question_text, display_order, input_type, is_required, is_active)
VALUES
    ('CALM_MIND', 'GOOD_MEMORY', '떠올리면 마음이 편해지는 기억이 있나요?', 1, 'TEXT', FALSE, TRUE),
    ('CALM_MIND', 'HOPEFUL_GOAL', '앞으로 기대하고 싶은 작은 목표는 무엇인가요?', 2, 'TEXT', FALSE, TRUE),
    ('CALM_MIND', 'SELF_MESSAGE', '지금 나에게 해주고 싶은 말은 무엇인가요?', 3, 'TEXT', FALSE, TRUE);

-- 6. 호흡하고 안정 찾기
INSERT INTO cbt_questions (tool_code, code, question_text, display_order, input_type, is_required, is_active)
VALUES
    ('BREATH_STABILIZE', 'BREATH_GUIDE', '천천히 숨을 들이마시고 내쉬며 몸의 긴장을 낮춰보세요', 1, 'GUIDE', FALSE, TRUE),
    ('BREATH_STABILIZE', 'SOUND_CHECK', '들리는 소리나 주변의 감각에 집중해보세요', 2, 'GUIDE', FALSE, TRUE),
    ('BREATH_STABILIZE', 'AFTER_STATE', '지금 몸과 마음의 상태는 어떤가요?', 3, 'TEXT', FALSE, TRUE);

-- =========================
-- 9. 인덱스 추가
-- =========================
CREATE INDEX idx_cbt_sessions_tool_code
    ON cbt_sessions(tool_code);

CREATE INDEX idx_cbt_questions_tool_code
    ON cbt_questions(tool_code);

CREATE INDEX idx_cbt_questions_tool_code_display_order
    ON cbt_questions(tool_code, display_order);