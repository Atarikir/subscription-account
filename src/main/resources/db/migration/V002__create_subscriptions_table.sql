CREATE TABLE IF NOT EXISTS subscriptions (
    id           uuid         PRIMARY KEY,
    service_name varchar(255) NOT NULL,
    user_id      uuid         NOT NULL,
    CONSTRAINT user_service_name_unique UNIQUE (service_name, user_id),
    CONSTRAINT fk_user_subscription FOREIGN KEY (user_id) REFERENCES users (id)
);