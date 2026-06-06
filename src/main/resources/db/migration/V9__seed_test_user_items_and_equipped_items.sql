DO $$
DECLARE
v_user_id BIGINT;
    v_hat_item_id BIGINT;
    v_clothes_item_id BIGINT;
    v_shoes_item_id BIGINT;
    v_background_item_id BIGINT;
    v_accessory_item_id BIGINT;

    v_hat_category_id BIGINT;
    v_clothes_category_id BIGINT;
    v_shoes_category_id BIGINT;
    v_background_category_id BIGINT;
    v_accessory_category_id BIGINT;
BEGIN
    -- =========================
    -- 1. 테스트 유저 조회
    -- accounts.email 기준으로 찾음
    -- =========================
SELECT u.user_id
INTO v_user_id
FROM users u
         JOIN accounts a ON a.account_id = u.account_id
WHERE a.email = 'test@kiwi.com';

IF v_user_id IS NULL THEN
        RAISE EXCEPTION '테스트 유저를 찾을 수 없습니다. accounts.email 값을 확인하세요';
END IF;

    -- =========================
    -- 2. 테스트 유저 kiwi_balance 지급
    -- =========================
UPDATE users
SET kiwi_balance = 5000
WHERE user_id = v_user_id;

-- =========================
-- 3. 카테고리 ID 조회
-- =========================
SELECT item_category_id INTO v_hat_category_id
FROM item_categories
WHERE name = '모자';

SELECT item_category_id INTO v_clothes_category_id
FROM item_categories
WHERE name = '옷';

SELECT item_category_id INTO v_shoes_category_id
FROM item_categories
WHERE name = '신발';

SELECT item_category_id INTO v_background_category_id
FROM item_categories
WHERE name = '배경';

SELECT item_category_id INTO v_accessory_category_id
FROM item_categories
WHERE name = '액세서리';

-- =========================
-- 4. 지급할 아이템 ID 조회
-- 이전 시드 기준 이름 사용
-- =========================
SELECT item_id INTO v_hat_item_id
FROM items
WHERE name = '기본 모자';

SELECT item_id INTO v_clothes_item_id
FROM items
WHERE name = '기본 티셔츠';

SELECT item_id INTO v_shoes_item_id
FROM items
WHERE name = '기본 운동화';

SELECT item_id INTO v_background_item_id
FROM items
WHERE name = '기본 방 배경';

SELECT item_id INTO v_accessory_item_id
FROM items
WHERE name = '기본 안경';

-- =========================
-- 5. 테스트 유저 보유 아이템 지급
-- UNIQUE(user_id, item_id) 제약 고려
-- =========================
INSERT INTO user_items (user_id, item_id, is_owned)
VALUES
    (v_user_id, v_hat_item_id, TRUE),
    (v_user_id, v_clothes_item_id, TRUE),
    (v_user_id, v_shoes_item_id, TRUE),
    (v_user_id, v_background_item_id, TRUE),
    (v_user_id, v_accessory_item_id, TRUE)
    ON CONFLICT (user_id, item_id) DO NOTHING;

-- =========================
-- 6. 테스트 유저 장착 아이템 세팅
-- 카테고리당 1개 장착 보장
-- =========================
INSERT INTO user_equipped_items (user_id, item_category_id, item_id)
VALUES
    (v_user_id, v_hat_category_id, v_hat_item_id),
    (v_user_id, v_clothes_category_id, v_clothes_item_id),
    (v_user_id, v_shoes_category_id, v_shoes_item_id),
    (v_user_id, v_background_category_id, v_background_item_id),
    (v_user_id, v_accessory_category_id, v_accessory_item_id)
    ON CONFLICT (user_id, item_category_id)
    DO UPDATE SET
    item_id = EXCLUDED.item_id,
               equipped_at = NOW();

END $$;