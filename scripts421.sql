alter table student add constraint age_constraint check (age>=16);
alter table student alter column name set not null;
alter table student add constraint name_constraint unique (name);
alter table faculty add constraint color_name_constraint unique (name, color);
ALTER TABLE student ALTER COLUMN age set default 20;
