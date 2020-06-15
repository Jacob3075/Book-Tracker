create table if not exists book_category
(
    books_id      bigint not null,
    categories_id bigint not null,
    primary key (books_id, categories_id),
    constraint fk_books_has_categories_books
        foreign key (books_id) references books (id),
    constraint fk_books_has_categories_categories1
        foreign key (categories_id) references categories (id)
);

create index fk_books_has_categories_books_idx
    on book_category (books_id);

create index fk_books_has_categories_categories1_idx
    on book_category (categories_id);

