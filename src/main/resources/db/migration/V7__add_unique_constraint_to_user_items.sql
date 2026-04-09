-- user 한 명이 같은 item을 중복 보유하지 못하도록 제약 추가
ALTER TABLE user_items
    ADD CONSTRAINT uq_user_items_user_item UNIQUE (user_id, item_id);