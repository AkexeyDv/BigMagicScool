-- liquibase formatted sql
-- changeset sconnor:1
CREATE INDEX sdudent_name_idx ON USING GIST student(name);