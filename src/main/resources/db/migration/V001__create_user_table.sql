CREATE TABLE IF NOT EXISTS users (
    id    uuid         PRIMARY KEY,
    email varchar(50) NOT NULL UNIQUE,
    first_name varchar(255) NOT NULL,
    last_name  varchar(255) NOT NULL
);