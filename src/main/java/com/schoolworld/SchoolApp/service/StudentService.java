package com.schoolworld.SchoolApp.service;

import com.schoolworld.SchoolApp.domain.Exam;
import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.dto.ExamDto;
import com.schoolworld.SchoolApp.domain.dto.StudentDto;
import com.schoolworld.SchoolApp.domain.dto.StudentRequestDto;
import com.schoolworld.SchoolApp.mappers.ExamMapper;
import com.schoolworld.SchoolApp.mappers.StudentMapper;
import com.schoolworld.SchoolApp.repository.ExamRepo;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepo studentRepo;
    private final StudentMapper studentMapper;
    private final ExamRepo examRepo;
    private final SubjectRepo subjectRepo;
    private final ExamMapper examMapper;

    public StudentDto save(StudentRequestDto studentRequestDto) {
            Student student = StudentMapper.toEntity(studentRequestDto);
            student.setName(studentRequestDto.getName());
            student.setEmail(studentRequestDto.getEmail());
            studentRepo.save(student);
        return StudentMapper.toDto(student);
    }

    public Optional<StudentDto> findById(Long id) {
        Student foundStudent = studentRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Student with id: " + id + " not found"));
        return Optional.of(StudentMapper.toDto(foundStudent));
    }

    public Optional<StudentDto> findByEmail(String email) {
        Student foundStudent = studentRepo.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Student with email: " + email + " not found"));
        StudentDto studentDto = new StudentDto(foundStudent.getName(), foundStudent.getEmail());
        return Optional.of(StudentMapper.toDto(foundStudent));
    }

    public List<StudentDto> getStudentsWithExams() {
        List<Student> students = studentRepo.findAll();
        List<StudentDto> studentDtos = new ArrayList<>();

        for (Student student : students) {
            StudentDto studentDto = StudentMapper.toDto(student);
            List<Exam> exams = examRepo.findByStudent(student);
            List<ExamDto> examDtos = exams.stream()
                    .map(ExamMapper::toDto)
                    .collect(Collectors.toList());
            studentDto.setExams(examDtos);
            studentDtos.add(studentDto);
        }
        return studentDtos;
    }


    public void deleteStudent(Long id) {
        Student studentToDelete = studentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with id: " + id + " not found"));
        studentRepo.deleteById(studentToDelete.getId());
    }

    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Student studentToUpdate = studentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with id: " + id + " not found"));

        studentToUpdate.setName(studentDto.getName());
        studentToUpdate.setEmail(studentDto.getEmail());

        Student savedStudent = studentRepo.save(studentToUpdate);
        return StudentMapper.toDto(savedStudent);
    }
}
