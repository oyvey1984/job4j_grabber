CREATE TABLE IF NOT EXISTS post (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    link TEXT UNIQUE,
    description TEXT,
    time TIMESTAMP NOT NULL
);