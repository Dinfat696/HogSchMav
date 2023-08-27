create TABLE users (
id INT PRIMARY KEY,
fisr_name TEXT PRIMARY KEY,
age INT,
driving_license BOOLEAN,
id_car INT REFERENCE car (id)

);
create TABLE cars (
id INT,
marka TEXT,
model varchar(128),
prise INT
);

select student.name, student.age,faculty.name
from student
inner join faculty on student.faculty_id = faculty.id
select student.name
from student
inner join avatar on avatar."id" = avatar.student_id