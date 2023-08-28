-- liquibase formatted sql

-- changeset dinar:1
create index ind_stud on student(name);
create index ind_fucu on faculty(name,color);