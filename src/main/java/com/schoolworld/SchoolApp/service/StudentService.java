package com.schoolworld.SchoolApp.service;

import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.dto.StudentDto;
import com.schoolworld.SchoolApp.domain.dto.StudentRequestDto;
import com.schoolworld.SchoolApp.exceptions.StudentWithSuchEmailExists;
import com.schoolworld.SchoolApp.exceptions.StudentsNotFoundException;
import com.schoolworld.SchoolApp.mappers.StudentMapper;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepo studentRepo;
    private final StudentMapper studentMapper;

    public StudentDto save(StudentRequestDto studentRequestDto) throws StudentWithSuchEmailExists {
        if (studentRepo.findByEmail(studentRequestDto.getEmail()).isPresent()) {
            throw new StudentWithSuchEmailExists("Student with email :" + studentRequestDto.getEmail() + "exists");
        }
            Student student = studentMapper.toEntity(studentRequestDto);
            student.setName(studentRequestDto.getName());
            student.setEmail(studentRequestDto.getEmail());
            studentRepo.save(student);
        return studentMapper.toDto(student);
    }

    public Optional<StudentDto> findById(Long id) {
        Student foundStudent = studentRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Student with id: " + id + " not found"));
        return Optional.of(studentMapper.toDto(foundStudent));
    }

    public Optional<StudentDto> findByEmail(String email) {
        Student foundStudent = studentRepo.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Student with email: " + email + " not found"));
        return Optional.of(studentMapper.toDto(foundStudent));
    }

    public List<StudentDto> getStudentsWithExams() throws StudentsNotFoundException {
        List<Student> students = studentRepo.findAll();
        if (students.isEmpty()){
            throw new StudentsNotFoundException("No students in database");
        }
        return studentRepo.findAll().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
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
        return studentMapper.toDto(savedStudent);
    }
}
