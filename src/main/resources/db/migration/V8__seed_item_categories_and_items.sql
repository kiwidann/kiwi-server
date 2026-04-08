-- =========================
-- item_categories 초기 데이터
-- =========================
INSERT INTO item_categories (name)
VALUES
    ('모자'),
    ('옷'),
    ('신발'),
    ('배경'),
    ('액세서리');

-- =========================
-- items 초기 데이터
-- =========================
INSERT INTO items (item_category_id, name, description, image_url, price, is_active)
VALUES
    -- 모자
    (
        (SELECT item_category_id FROM item_categories WHERE name = '모자'),
        '기본 모자',
        '심플한 기본 모자',
        '/images/items/hat_basic.png',
        100,
        TRUE
    ),
    (
        (SELECT item_category_id FROM item_categories WHERE name = '모자'),
        '밀짚모자',
        '가볍고 산뜻한 밀짚모자',
        '/images/items/hat_straw.png',
        300,
        TRUE
    ),
    (
        (SELECT item_category_id FROM item_categories WHERE name = '모자'),
        '곰돌이 후드',
        '귀여운 곰돌이 모양 후드',
        '/images/items/hat_bear_hood.png',
        500,
        TRUE
    ),

    -- 옷
    (
        (SELECT item_category_id FROM item_categories WHERE name = '옷'),
        '기본 티셔츠',
        '어디에나 잘 어울리는 기본 티셔츠',
        '/images/items/clothes_basic_tshirt.png',
        150,
        TRUE
    ),
    (
        (SELECT item_category_id FROM item_categories WHERE name = '옷'),
        '후드티',
        '편안하게 입을 수 있는 후드티',
        '/images/items/clothes_hoodie.png',
        400,
        TRUE
    ),
    (
        (SELECT item_category_id FROM item_categories WHERE name = '옷'),
        '겨울 코트',
        '포근한 느낌의 겨울 코트',
        '/images/items/clothes_winter_coat.png',
        700,
        TRUE
    ),

    -- 신발
    (
        (SELECT item_category_id FROM item_categories WHERE name = '신발'),
        '기본 운동화',
        '가볍고 편한 운동화',
        '/images/items/shoes_basic_sneakers.png',
        200,
        TRUE
    ),
    (
        (SELECT item_category_id FROM item_categories WHERE name = '신발'),
        '털부츠',
        '따뜻한 겨울용 털부츠',
        '/images/items/shoes_fur_boots.png',
        450,
        TRUE
    ),
    (
        (SELECT item_category_id FROM item_categories WHERE name = '신발'),
        '슬리퍼',
        '편하게 신을 수 있는 슬리퍼',
        '/images/items/shoes_slippers.png',
        120,
        TRUE
    ),

    -- 배경
    (
        (SELECT item_category_id FROM item_categories WHERE name = '배경'),
        '기본 방 배경',
        '기본 제공 배경',
        '/images/items/background_basic_room.png',
        0,
        TRUE
    ),
    (
        (SELECT item_category_id FROM item_categories WHERE name = '배경'),
        '노을 배경',
        '따뜻한 노을이 보이는 배경',
        '/images/items/background_sunset.png',
        600,
        TRUE
    ),
    (
        (SELECT item_category_id FROM item_categories WHERE name = '배경'),
        '밤하늘 배경',
        '별이 반짝이는 밤하늘 배경',
        '/images/items/background_night_sky.png',
        800,
        TRUE
    ),

    -- 액세서리
    (
        (SELECT item_category_id FROM item_categories WHERE name = '액세서리'),
        '기본 안경',
        '단정한 느낌의 안경',
        '/images/items/accessory_glasses.png',
        250,
        TRUE
    ),
    (
        (SELECT item_category_id FROM item_categories WHERE name = '액세서리'),
        '리본',
        '포인트를 줄 수 있는 리본',
        '/images/items/accessory_ribbon.png',
        180,
        TRUE
    ),
    (
        (SELECT item_category_id FROM item_categories WHERE name = '액세서리'),
        '별 목걸이',
        '작은 별 장식의 목걸이',
        '/images/items/accessory_star_necklace.png',
        350,
        TRUE
    );