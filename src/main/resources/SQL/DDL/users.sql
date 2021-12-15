create table users
(
    name varchar not null,
    email varchar not null,
    password varchar not null,
    uuid varchar,
    role varchar,
    id serial not null
        constraint users_pk
            primary key
);

alter table users owner to postgres;

create unique index users_email_uindex
	on users (email);

create unique index users_username_uindex
	on users (name);

create unique index users_uuid_uindex
	on users (uuid);

