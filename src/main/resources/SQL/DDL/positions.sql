create table positions
(
    id serial not null
        constraint positions_pk
            primary key,
    name varchar not null,
    container_type varchar,
    beer_type varchar not null,
    alcohol_percentage double precision not null,
    bitterness integer not null,
    created timestamp,
    modified timestamp,
    beer_info json
);

alter table positions owner to postgres;
