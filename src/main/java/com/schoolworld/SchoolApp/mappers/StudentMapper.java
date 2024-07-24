package com.schoolworld.SchoolApp.mappers;

import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.dto.ExamDto;
import com.schoolworld.SchoolApp.domain.dto.StudentDto;
import com.schoolworld.SchoolApp.domain.dto.StudentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {
    @Autowired
    private ExamMapper examMapper;
    public Student toEntity(StudentRequestDto studentRequestDto) {
        if (studentRequestDto == null) {
            return null;
        }

        Student student = new Student("Jan", "jan@op.pl");
        student.setName(studentRequestDto.getName());
        student.setEmail(studentRequestDto.getEmail());

        return student;
    }

    public StudentDto toDto(Student student) {
        if (student == null) {
            return null;
        }

        StudentDto studentDto = new StudentDto();
        studentDto.setName(student.getName());
        studentDto.setEmail(student.getEmail());
        List<ExamDto> examDtos = student.getExams().stream()
                .map(examMapper::toDto)
                .collect(Collectors.toList());
        studentDto.setExams(examDtos);
        return studentDto;
    }
}
