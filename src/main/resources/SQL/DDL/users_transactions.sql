create table users_transactions
(
    id serial not null
        constraint transactions_pk
            primary key,
    quantity json,
    created timestamp,
    user_id integer
        constraint users_transactions_users__fk
            references users,
    position_id integer
        constraint users_transactions_positions__fk
            references positions,
    quantity_class_type varchar
);

alter table users_transactions owner to postgres;

create unique index transactions_id_uindex
	on users_transactions (id);



