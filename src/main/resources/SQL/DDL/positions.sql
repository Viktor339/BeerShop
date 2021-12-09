create table positions
(
	id serial not null
		constraint positions_pk
			primary key,
	name varchar not null,
	containertype varchar,
	beertype varchar not null,
	alcoholpercentage double precision not null,
	bitterness integer not null,
	created date,
	modified date,
	beerinfo json
);

alter table positions owner to postgres;

create unique index positions_id_uindex
	on positions (id);