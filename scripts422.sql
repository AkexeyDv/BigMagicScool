create table avto(id integer primary key, stamp varchar not null,
model varchar not null, cost integer chack(cost>0);
create table srive(id integer primary key, name varchar not null,
age smallint check (age>=18), license boolean default 0,id_avto integer references avto(id));
