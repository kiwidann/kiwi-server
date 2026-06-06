-- V3__refactor_cbt_and_add_item_system.sql
-- CBT 구조를 단일 테이블에서 세션/질문/답변 구조로 개편하고,
-- diaries를 record당 1개만 작성 가능하도록 제약을 추가하며,
-- item 시스템 관련 테이블을 생성한다.

-- =========================
-- 1. diaries: 하루 1개 일기 제한
-- =========================
ALTER TABLE diaries
    ADD CONSTRAINT uq_diaries_record UNIQUE (record_id);

-- =========================
-- 2. 기존 CBT 구조 제거
-- =========================
DROP TRIGGER IF EXISTS trg_cbt_records_updated_at ON cbt_records;
DROP TABLE cbt_records;

-- =========================
-- 3. CBT 세션 테이블 생성
-- records 1 : N cbt_sessions
-- tags 1 : N cbt_sessions
-- =========================
CREATE TABLE cbt_sessions (
                              cbt_session_id BIGSERIAL PRIMARY KEY,
                              record_id BIGINT NOT NULL,
                              tag_id BIGINT NOT NULL,
                              before_emotion_score INT,
                              after_emotion_score INT,
                              created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                              updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                              CONSTRAINT fk_cbt_sessions_record
                                  FOREIGN KEY (record_id)
                                      REFERENCES records(record_id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_cbt_sessions_tag
                                  FOREIGN KEY (tag_id)
                                      REFERENCES tags(tag_id),

                              CONSTRAINT chk_cbt_sessions_before_score
                                  CHECK (before_emotion_score BETWEEN 0 AND 10),

                              CONSTRAINT chk_cbt_sessions_after_score
                                  CHECK (after_emotion_score BETWEEN 0 AND 10)
);

-- =========================
-- 4. CBT 질문 마스터 테이블 생성
-- =========================
CREATE TABLE cbt_questions (
                               question_id BIGSERIAL PRIMARY KEY,
                               code VARCHAR(50) NOT NULL UNIQUE,
                               question_text TEXT NOT NULL,
                               display_order INT NOT NULL,
                               is_required BOOLEAN NOT NULL DEFAULT TRUE,
                               is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- =========================
-- 5. CBT 답변 테이블 생성
-- cbt_sessions 1 : N cbt_answers
-- cbt_questions 1 : N cbt_answers
-- =========================
CREATE TABLE cbt_answers (
                             answer_id BIGSERIAL PRIMARY KEY,
                             cbt_session_id BIGINT NOT NULL,
                             question_id BIGINT NOT NULL,
                             answer_text TEXT,
                             created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                             updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                             CONSTRAINT fk_cbt_answers_session
                                 FOREIGN KEY (cbt_session_id)
                                     REFERENCES cbt_sessions(cbt_session_id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_cbt_answers_question
                                 FOREIGN KEY (question_id)
                                     REFERENCES cbt_questions(question_id)
                                     ON DELETE CASCADE,

                             CONSTRAINT uq_cbt_session_question UNIQUE (cbt_session_id, question_id)
);

-- =========================
-- 6. CBT 질문 초기 데이터 삽입
-- =========================
INSERT INTO cbt_questions (code, question_text, display_order, is_required, is_active)
VALUES
    ('SITUATION', '어떤 상황이 있었나요?', 1, TRUE, TRUE),
    ('AUTOMATIC_THOUGHT', '그때 어떤 생각이 들었나요?', 2, TRUE, TRUE),
    ('EVIDENCE_FOR', '그 생각을 지지하는 근거는 무엇인가요?', 3, TRUE, TRUE),
    ('EVIDENCE_AGAINST', '그 생각에 반대되는 근거는 무엇인가요?', 4, TRUE, TRUE),
    ('ALTERNATIVE_THOUGHT', '다르게 생각해볼 수 있는 방법은 무엇인가요?', 5, TRUE, TRUE),
    ('REFLECTION', '이번 과정을 통해 느낀 점은 무엇인가요?', 6, FALSE, TRUE);

-- =========================
-- 7. item 카테고리 테이블 생성
-- =========================
CREATE TABLE item_categories (
                                 item_category_id BIGSERIAL PRIMARY KEY,
                                 name VARCHAR(100) NOT NULL UNIQUE,
                                 created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- =========================
-- 8. items 테이블 생성
-- item_categories 1 : N items
-- =========================
CREATE TABLE items (
                       item_id BIGSERIAL PRIMARY KEY,
                       item_category_id BIGINT NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       description VARCHAR(255),
                       image_url VARCHAR(255),
                       price INT NOT NULL,
                       is_active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                       CONSTRAINT fk_items_category
                           FOREIGN KEY (item_category_id)
                               REFERENCES item_categories(item_category_id)
                               ON DELETE RESTRICT
);

-- =========================
-- 9. user_items 테이블 생성
-- users 1 : N user_items
-- items 1 : N user_items
-- =========================
CREATE TABLE user_items (
                            user_item_id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            item_id BIGINT NOT NULL,
                            acquired_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                            is_owned BOOLEAN NOT NULL DEFAULT TRUE,

                            CONSTRAINT fk_user_items_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(user_id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_user_items_item
                                FOREIGN KEY (item_id)
                                    REFERENCES items(item_id)
                                    ON DELETE CASCADE
);

-- =========================
-- 10. user_equipped_items 테이블 생성
-- users 1 : N user_equipped_items
-- item_categories 1 : N user_equipped_items
-- items 1 : N user_equipped_items
-- 카테고리당 1개 착용 보장: UNIQUE(user_id, item_category_id)
-- =========================
CREATE TABLE user_equipped_items (
                                     user_equipped_item_id BIGSERIAL PRIMARY KEY,
                                     user_id BIGINT NOT NULL,
                                     item_category_id BIGINT NOT NULL,
                                     item_id BIGINT NOT NULL,
                                     equipped_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                                     CONSTRAINT fk_user_equipped_items_user
                                         FOREIGN KEY (user_id)
                                             REFERENCES users(user_id)
                                             ON DELETE CASCADE,

                                     CONSTRAINT fk_user_equipped_items_category
                                         FOREIGN KEY (item_category_id)
                                             REFERENCES item_categories(item_category_id)
                                             ON DELETE CASCADE,

                                     CONSTRAINT fk_user_equipped_items_item
                                         FOREIGN KEY (item_id)
                                             REFERENCES items(item_id)
                                             ON DELETE CASCADE,

                                     CONSTRAINT uq_user_equipped_category UNIQUE (user_id, item_category_id)
);

-- =========================
-- 11. 인덱스 추가
-- =========================
CREATE INDEX idx_cbt_sessions_record_id ON cbt_sessions(record_id);
CREATE INDEX idx_cbt_sessions_tag_id ON cbt_sessions(tag_id);

CREATE INDEX idx_cbt_answers_session_id ON cbt_answers(cbt_session_id);
CREATE INDEX idx_cbt_answers_question_id ON cbt_answers(question_id);

CREATE INDEX idx_items_category_id ON items(item_category_id);

CREATE INDEX idx_user_items_user_id ON user_items(user_id);
CREATE INDEX idx_user_items_item_id ON user_items(item_id);

CREATE INDEX idx_user_equipped_items_user_id ON user_equipped_items(user_id);
CREATE INDEX idx_user_equipped_items_category_id ON user_equipped_items(item_category_id);
CREATE INDEX idx_user_equipped_items_item_id ON user_equipped_items(item_id);

-- =========================
-- 12. updated_at 트리거 추가
-- =========================
CREATE TRIGGER trg_cbt_sessions_updated_at
    BEFORE UPDATE ON cbt_sessions
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trg_cbt_answers_updated_at
    BEFORE UPDATE ON cbt_answers
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trg_items_updated_at
    BEFORE UPDATE ON items
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();