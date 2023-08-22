alter TABLE student
ADD CONSTRAINT age_constraint CHECK (age > 16);
ADD CONSTRAINT name_unique UNIQUE (name);
ADD COLUMN name_not_null NOTNULL (name);
alter TABLE faculty
ADD CONSTRAINT name_color_unique UNIQUE (name, color);
alter TABLE student alter COLUMN age SET DEFAULT 20;