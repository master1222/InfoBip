drop database totorotournamentdb;
drop user totoro;
create user totoro with password 'letsplay';
create database totorotournamentdb with template=template0 owner=totoro;
\connect totorotournamentdb;
alter default privileges grant all on tables to totoro;
alter default privileges grant all on sequences to totoro;

create table et_players(
player_id integer primary key not null,
name varchar(50) not null,
age integer,
score integer
);

create table et_matches(
match_id integer primary key not null,
first_player_id integer not null,
second_player_id integer not null,
result varchar(3) null
);
alter table et_matches add constraint match_first_player_fk
foreign key (first_player_id) references et_players(player_id);
alter table et_matches add constraint match_second_player_fk
foreign key (second_player_id) references et_players(player_id);

create sequence et_players_seq increment 1 start 1;
create sequence et_matches_seq increment 1 start 1;