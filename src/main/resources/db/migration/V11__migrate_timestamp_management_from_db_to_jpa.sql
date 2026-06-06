-- V11__migrate_timestamp_management_from_db_to_jpa.sql
-- created_at / updated_at 관리 책임을 DB trigger/default 에서 JPA Auditing으로 이전한다

-- =========================
-- 1. updated_at 트리거 제거
-- =========================
DROP TRIGGER IF EXISTS trg_accounts_updated_at ON accounts;
DROP TRIGGER IF EXISTS trg_users_updated_at ON users;
DROP TRIGGER IF EXISTS trg_records_updated_at ON records;
DROP TRIGGER IF EXISTS trg_diaries_updated_at ON diaries;
DROP TRIGGER IF EXISTS trg_cbt_sessions_updated_at ON cbt_sessions;
DROP TRIGGER IF EXISTS trg_cbt_answers_updated_at ON cbt_answers;
DROP TRIGGER IF EXISTS trg_items_updated_at ON items;

-- =========================
-- 2. created_at / updated_at DEFAULT 제거
--    앞으로는 JPA Auditing이 값을 넣는다
-- =========================

-- accounts
ALTER TABLE accounts
    ALTER COLUMN created_at DROP DEFAULT,
ALTER COLUMN updated_at DROP DEFAULT;

-- users
ALTER TABLE users
    ALTER COLUMN created_at DROP DEFAULT,
ALTER COLUMN updated_at DROP DEFAULT;

-- records
ALTER TABLE records
    ALTER COLUMN created_at DROP DEFAULT,
ALTER COLUMN updated_at DROP DEFAULT;

-- diaries
ALTER TABLE diaries
    ALTER COLUMN created_at DROP DEFAULT,
ALTER COLUMN updated_at DROP DEFAULT;

-- keywords
ALTER TABLE keywords
    ALTER COLUMN created_at DROP DEFAULT;

-- tags
ALTER TABLE tags
    ALTER COLUMN created_at DROP DEFAULT;

-- kiwi_transactions
ALTER TABLE kiwi_transactions
    ALTER COLUMN created_at DROP DEFAULT;

-- cbt_sessions
ALTER TABLE cbt_sessions
    ALTER COLUMN created_at DROP DEFAULT,
ALTER COLUMN updated_at DROP DEFAULT;

-- cbt_answers
ALTER TABLE cbt_answers
    ALTER COLUMN created_at DROP DEFAULT,
ALTER COLUMN updated_at DROP DEFAULT;

-- item_categories
ALTER TABLE item_categories
    ALTER COLUMN created_at DROP DEFAULT;

-- items
ALTER TABLE items
    ALTER COLUMN created_at DROP DEFAULT,
ALTER COLUMN updated_at DROP DEFAULT;

-- =========================
-- 3. 더 이상 사용하지 않는 공통 함수 제거
-- =========================
DROP FUNCTION IF EXISTS update_timestamp();