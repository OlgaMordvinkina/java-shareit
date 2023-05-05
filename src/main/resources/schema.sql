DROP TABLE IF EXISTS users, items, bookings, comments;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY primary key,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY primary key,
    name        varchar(255) NOT NULL,
    description varchar(512) NOT NULL,
    available   boolean,
    owner_id    BIGINT       NOT NULL,
    request_id  BIGINT,
    CONSTRAINT FK_ITEM_FOR_OWNER FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY primary key,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT,
    booker_id  BIGINT,
    status     varchar(128),
    CONSTRAINT FK_BOOKING_FOR_BOOKER FOREIGN KEY (booker_id) REFERENCES users (id),
    CONSTRAINT FK_BOOKING_FOR_ITEM FOREIGN KEY (item_id) REFERENCES items (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY primary key,
    text      VARCHAR(500)                                   NOT NULL,
    item_id   BIGINT REFERENCES items (id) ON DELETE CASCADE NOT NULL,
    author_id BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT FK_BOOKING_FOR_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id)
);