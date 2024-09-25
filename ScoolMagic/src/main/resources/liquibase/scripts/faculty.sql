-- liquibase formatted sql

-- changeset sconnor:1
CREATE INDEX faculty_idx_name_color ON faculty (name,color);