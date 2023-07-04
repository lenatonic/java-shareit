
create table if not exists users
(
    id_user    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name_user  VARCHAR NOT NULL,
    email_user VARCHAR NOT NULL UNIQUE
);

create table if not exists items
(
    id_item     bigint generated by default as identity
        constraint items_pk
            primary key,
    name_item   varchar(255) not null,
    description varchar(255) not null,
    available   boolean,
    owner       bigint       not null
        constraint items_users_id_user_fk
            references users,
    request_id  bigint,
    CONSTRAINT fk_items_to_requests FOREIGN KEY (request_id) REFERENCES (request_id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    BIGINT                      NOT NULL,
    booker_id  BIGINT                      NOT NULL,
    status     VARCHAR                     NOT NULL,
    CONSTRAINT fk_bookings_to_items FOREIGN KEY (item_id) REFERENCES items (id_item),
    CONSTRAINT fk_bookings_to_users FOREIGN KEY (booker_id) REFERENCES users (id_user),
    CONSTRAINT constr_status CHECK (status IN ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED'))
);

CREATE TABLE IF NOT EXISTS comments
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text        VARCHAR                     NOT NULL,
    item_id     BIGINT                      NOT NULL,
    author_id   BIGINT                      NOT NULL,
    create_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_comments_to_items FOREIGN KEY (item_id) REFERENCES items (id_item),
    CONSTRAINT fk_comments_to_users FOREIGN KEY (author_id) REFERENCES users (id_user)
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description VARCHAR NOT NULL,
    requestor_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requestor_id) REFERENCES users (id_user)
)