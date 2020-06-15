create table if not exists books
(
    id          bigint auto_increment
        primary key,
    name        varchar(255) null,
    description varchar(255) null
);

