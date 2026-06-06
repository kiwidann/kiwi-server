-- 테스트용 계정 1개 시드 데이터
-- 비밀번호 원문 예시: Test1234!

WITH inserted_account AS (
INSERT INTO accounts (
    email,
    password_hash,
    is_verified,
    verified_at,
    is_deleted,
    deleted_at
)
VALUES (
    'test@kiwi.com',
    '$2a$10$7W/pC2V1SMqFTQp8fam5juseYCAC33DMYrrIpRRRQSROgEfHc8zR6',
    TRUE,
    NOW(),
    FALSE,
    NULL
    )
    RETURNING account_id
    )
INSERT INTO users (
    account_id,
    nickname,
    profile_image_url,
    kiwi_balance
)
SELECT
    account_id,
    '테스트유저',
    NULL,
    0
FROM inserted_account;