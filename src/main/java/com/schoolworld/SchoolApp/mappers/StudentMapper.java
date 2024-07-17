package com.schoolworld.SchoolApp.mappers;

import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.dto.StudentDto;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public Student toEntity(StudentDto studentDto) {
        Student student = new Student();
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        return student;
    }

    public StudentDto toDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setName(student.getName());
        studentDto.setEmail(student.getEmail());
        return studentDto;
    }
}
