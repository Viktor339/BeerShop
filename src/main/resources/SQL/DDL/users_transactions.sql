create table users_transactions
(
    id serial not null
        constraint transactions_pk
            primary key,
    name varchar,
    quantity double precision,
    date date,
    user_id integer
        constraint transactions_user_id_fkey
            references users
);

alter table users_transactions owner to postgres;

create unique index transactions_id_uindex
	on users_transactions (id);