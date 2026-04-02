ALTER TABLE diaries
DROP COLUMN is_deleted,
    DROP COLUMN deleted_at;

ALTER TABLE diaries
    ALTER COLUMN title SET NOT NULL;