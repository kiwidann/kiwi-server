-- V1__init.sql
-- Kiwi 프로젝트 초기 스키마 생성
-- 계정, 사용자, 하루 기록, 일기, 키워드, CBT 기록, 키위 재화 이력 테이블을 생성한다

-- =========================
-- ENUM TYPES
-- =========================
CREATE TYPE kiwi_tx_type AS ENUM (
    'EARN_DIARY',
    'EARN_CBT',
    'USE_ITEM',
    'REFUND_ITEM',
    'ADMIN_ADJUST'
);

-- =========================
-- COMMON FUNCTION
-- updated_at 자동 갱신용
-- =========================
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- =========================
-- ACCOUNTS (인증)
-- =========================
CREATE TABLE accounts (
                          account_id BIGSERIAL PRIMARY KEY,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password_hash VARCHAR(255) NOT NULL,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                          deleted_at TIMESTAMPTZ
);

-- =========================
-- USERS (실제 사용자)
-- =========================
CREATE TABLE users (
                       user_id BIGSERIAL PRIMARY KEY,
                       account_id BIGINT NOT NULL UNIQUE,
                       nickname VARCHAR(50) NOT NULL,
                       profile_image_url VARCHAR(255),
                       kiwi_balance INT NOT NULL DEFAULT 0,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                       CONSTRAINT fk_users_account
                           FOREIGN KEY (account_id)
                               REFERENCES accounts(account_id)
                               ON DELETE CASCADE
);

-- =========================
-- RECORDS (하루 기록)
-- =========================
CREATE TABLE records (
                         record_id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         record_date DATE NOT NULL,
                         mood_score INT NOT NULL,
                         created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                         updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                         CONSTRAINT fk_records_user
                             FOREIGN KEY (user_id)
                                 REFERENCES users(user_id)
                                 ON DELETE CASCADE,

                         CONSTRAINT uq_user_date UNIQUE (user_id, record_date),
                         CONSTRAINT chk_mood_score CHECK (mood_score BETWEEN 1 AND 10)
);

-- =========================
-- DIARIES (일기)
-- =========================
CREATE TABLE diaries (
                         diary_id BIGSERIAL PRIMARY KEY,
                         record_id BIGINT NOT NULL,
                         title VARCHAR(100),
                         content TEXT NOT NULL,
                         created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                         updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                         is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                         deleted_at TIMESTAMPTZ,

                         CONSTRAINT fk_diaries_record
                             FOREIGN KEY (record_id)
                                 REFERENCES records(record_id)
                                 ON DELETE CASCADE
);

-- =========================
-- KEYWORDS (사용자별 키워드)
-- =========================
CREATE TABLE keywords (
                          keyword_id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          name VARCHAR(50) NOT NULL,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                          CONSTRAINT fk_keywords_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(user_id)
                                  ON DELETE CASCADE,

                          CONSTRAINT uq_user_keyword UNIQUE (user_id, name)
);

-- =========================
-- RECORD_KEYWORDS (N:M)
-- =========================
CREATE TABLE record_keywords (
                                 record_id BIGINT NOT NULL,
                                 keyword_id BIGINT NOT NULL,

                                 PRIMARY KEY (record_id, keyword_id),

                                 CONSTRAINT fk_rk_record
                                     FOREIGN KEY (record_id)
                                         REFERENCES records(record_id)
                                         ON DELETE CASCADE,

                                 CONSTRAINT fk_rk_keyword
                                     FOREIGN KEY (keyword_id)
                                         REFERENCES keywords(keyword_id)
                                         ON DELETE CASCADE
);

-- =========================
-- TAGS (CBT 원인 - 공용)
-- =========================
CREATE TABLE tags (
                      tag_id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE,
                      created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- =========================
-- CBT_RECORDS
-- =========================
CREATE TABLE cbt_records (
                             cbt_record_id BIGSERIAL PRIMARY KEY,
                             record_id BIGINT NOT NULL,
                             tag_id BIGINT,

                             situation TEXT NOT NULL,
                             automatic_thought TEXT NOT NULL,
                             evidence_for TEXT,
                             evidence_against TEXT,
                             alternative_thought TEXT,
                             reflection TEXT,

                             before_emotion_score INT,
                             after_emotion_score INT,

                             created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                             updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                             CONSTRAINT fk_cbt_record
                                 FOREIGN KEY (record_id)
                                     REFERENCES records(record_id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_cbt_tag
                                 FOREIGN KEY (tag_id)
                                     REFERENCES tags(tag_id),

                             CONSTRAINT chk_before_score CHECK (before_emotion_score BETWEEN 0 AND 100),
                             CONSTRAINT chk_after_score CHECK (after_emotion_score BETWEEN 0 AND 100)
);

-- =========================
-- KIWI_TRANSACTIONS (이력)
-- =========================
CREATE TABLE kiwi_transactions (
                                   tx_id BIGSERIAL PRIMARY KEY,
                                   user_id BIGINT NOT NULL,
                                   amount INT NOT NULL,
                                   type kiwi_tx_type NOT NULL,
                                   created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                                   CONSTRAINT fk_kiwi_user
                                       FOREIGN KEY (user_id)
                                           REFERENCES users(user_id)
                                           ON DELETE CASCADE
);

-- =========================
-- INDEXES
-- =========================
CREATE INDEX idx_records_user_id ON records(user_id);
CREATE INDEX idx_records_record_date ON records(record_date);

CREATE INDEX idx_diaries_record_id ON diaries(record_id);

CREATE INDEX idx_keywords_user_id ON keywords(user_id);

CREATE INDEX idx_record_keywords_keyword_id ON record_keywords(keyword_id);

CREATE INDEX idx_cbt_records_record_id ON cbt_records(record_id);
CREATE INDEX idx_cbt_records_tag_id ON cbt_records(tag_id);

CREATE INDEX idx_kiwi_transactions_user_id ON kiwi_transactions(user_id);
CREATE INDEX idx_kiwi_transactions_created_at ON kiwi_transactions(created_at);

-- =========================
-- TRIGGERS FOR updated_at
-- =========================
CREATE TRIGGER trg_accounts_updated_at
    BEFORE UPDATE ON accounts
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trg_records_updated_at
    BEFORE UPDATE ON records
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trg_diaries_updated_at
    BEFORE UPDATE ON diaries
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trg_cbt_records_updated_at
    BEFORE UPDATE ON cbt_records
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();