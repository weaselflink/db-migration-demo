CREATE SEQUENCE hibernate_sequence START 42 INCREMENT 1;

CREATE TABLE important (
    id bigint NOT NULL PRIMARY KEY,
    time timestamp NOT NULL,
    payload bytea NOT NULL
);
