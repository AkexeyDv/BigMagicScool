select student.name, student.name ,  faculty.name from student inner join faculty on student.faculty_id=faculty.id;
select avatar.id,student.name from avatar
inner join student  on avatar.student_id =student.id;
