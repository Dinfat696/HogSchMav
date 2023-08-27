alter TABLE student
ADD CONSTRAINT age_constraint CHECK (age > 16);
ADD CONSTRAINT name_unique UNIQUE (name);
alter TABLE student alter column name SET NOTNULL;
alter TABLE faculty
ADD CONSTRAINT name_color_unique UNIQUE (name, color);
alter TABLE student alter COLUMN age SET DEFAULT 20;
