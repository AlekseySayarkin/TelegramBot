drop table if exists Cities;
drop table if exists Countries;

create table Countries (
   cn_id smallint primary key auto_increment,
   cn_name varchar(32) not null unique
);

create table Cities (
    ct_id smallint primary key auto_increment,
    ct_name varchar(32) not null unique,
    ct_info varchar(1000) not null,
    cn_id smallint references Countries(cn_id) on delete set null
);