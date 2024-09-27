-- liquibase formatted sql

-- changeset student1:1
DROP INDEX faculty_idx_name_color;
CREATE INDEX faculty_idx_name_color ON faculty (name,color);