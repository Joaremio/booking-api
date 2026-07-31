CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE resources (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           description VARCHAR(255) NOT NULL,
                           capacity INTEGER NOT NULL,
                           price_per_hour NUMERIC(10,2) NOT NULL,
                           active BOOLEAN NOT NULL DEFAULT TRUE
);