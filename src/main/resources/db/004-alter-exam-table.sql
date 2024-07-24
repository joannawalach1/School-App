ALTER TABLE exam
    ADD CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES student(id);

ALTER TABLE exam
    ADD CONSTRAINT fk_subject FOREIGN KEY (subject_id) REFERENCES subject(id);