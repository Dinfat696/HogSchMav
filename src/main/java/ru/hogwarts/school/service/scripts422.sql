create table users (
id int primary key,
fisr_name text primary key,
age int,
driving_license boolean,
id_car int REFERENCE car (id)

);
create table cars (
id int primary key,
marka text,
model varchar(128),
prise int
);

select student.name, student.age,faculty.name
from student
left join faculty on student.faculty_id = faculty.id;

select student.name as student_name, student."age" as student_age
from student
inner join avatar  on student."id" = avatar.student_id;