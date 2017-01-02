CREATE TABLE rss_items (
    id INTEGER NOT NULL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    link VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    creator VARCHAR(20) NOT NULL,
    pub_date VARCHAR(20) NOT NULL,
    guid VARCHAR(50) NOT NULL
);

CREATE UNIQUE INDEX unq_rss_items_guid ON rss_items (guid);