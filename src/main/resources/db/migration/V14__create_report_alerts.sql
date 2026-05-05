CREATE TYPE report_alert_type AS ENUM (
    'LOW_EMOTION_STREAK',
    'REPEATED_NEGATIVE_KEYWORD',
    'LOW_CBT_IMPROVEMENT',
    'LOW_RECORD_ACTIVITY'
);

CREATE TABLE report_alerts (
                               report_alert_id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               type report_alert_type NOT NULL,
                               title VARCHAR(100) NOT NULL,
                               message VARCHAR(500) NOT NULL,
                               related_start_date DATE,
                               related_end_date DATE,
                               is_read BOOLEAN NOT NULL DEFAULT FALSE,
                               read_at TIMESTAMPTZ,
                               is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                               deleted_at TIMESTAMPTZ,
                               created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                               CONSTRAINT fk_report_alerts_user
                                   FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE INDEX idx_report_alerts_user_id
    ON report_alerts(user_id);

CREATE INDEX idx_report_alerts_user_id_is_read
    ON report_alerts(user_id, is_read);

CREATE INDEX idx_report_alerts_user_id_created_at
    ON report_alerts(user_id, created_at DESC);

CREATE INDEX idx_report_alerts_user_id_is_deleted
    ON report_alerts(user_id, is_deleted);