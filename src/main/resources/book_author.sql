create table if not exists book_author
(
    books_id   bigint not null,
    authors_id bigint not null,
    primary key (books_id, authors_id),
    constraint fk_books_has_authors_authors1
        foreign key (authors_id) references authors (id),
    constraint fk_books_has_authors_books1
        foreign key (books_id) references books (id)
);

create index fk_books_has_authors_authors1_idx
    on book_author (authors_id);

create index fk_books_has_authors_books1_idx
    on book_author (books_id);

