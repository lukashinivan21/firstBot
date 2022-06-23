-- liquibase formatted sql

-- changeSet ilukashin:1

CREATE TABLE notification
(
    id                     SERIAL PRIMARY KEY,
    chat_id                INTEGER NOT NULL,
    message                TEXT    NOT NULL,
    notification_send_time TIMESTAMP NOT NULL
);





