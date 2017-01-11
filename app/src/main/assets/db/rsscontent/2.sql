ALTER TABLE rss_items ADD COLUMN pub_date_millis BIGINT NOT NULL DEFAULT 0;

CREATE INDEX idx_rss_items_pub_date ON rss_items (pub_date_millis);