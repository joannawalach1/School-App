create table exam (
id serial PRIMARY KEY,
name_of_exam text,
date_of_exam timestamp,
student_id int,
subject_id int,
created timestamp,
updated timestamp
)