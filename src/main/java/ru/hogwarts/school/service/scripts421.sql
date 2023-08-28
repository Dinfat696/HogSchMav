alter table student
add CONSTRAINT age_constraint CHECK (age > 16);
alter table student
add CONSTRAINT name_unique UNIQUE (name);
alter table student alter column name SET NOTNULL;
alter table faculty
add CONSTRAINT name_color_unique UNIQUE (name, color);
alter table student alter COLUMN age SET DEFAULT 20;
