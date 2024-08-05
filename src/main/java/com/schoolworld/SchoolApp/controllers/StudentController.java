package com.schoolworld.SchoolApp.controllers;

import com.schoolworld.SchoolApp.domain.dto.StudentDto;
import com.schoolworld.SchoolApp.domain.dto.StudentRequestDto;
import com.schoolworld.SchoolApp.exceptions.StudentNotFoundException;
import com.schoolworld.SchoolApp.exceptions.StudentWithSuchEmailExists;
import com.schoolworld.SchoolApp.exceptions.StudentWithSuchIdExists;
import com.schoolworld.SchoolApp.exceptions.StudentsNotFoundException;
import com.schoolworld.SchoolApp.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentDto> saveStudent(@RequestBody StudentRequestDto studentRequestDto) throws StudentWithSuchEmailExists {
        StudentDto createdStudent = studentService.save(studentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<StudentDto> findById(@PathVariable Long id) {
        Optional<StudentDto> foundStudentDto = studentService.findById(id);

        return foundStudentDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/findByEmail/{email}")
    public ResponseEntity<StudentDto> findByEmail(@PathVariable String email) {
        Optional<StudentDto> foundStudentDto = studentService.findByEmail(email);

        return foundStudentDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping(value = "/students")
    public ResponseEntity<List<StudentDto>> findAll() throws StudentsNotFoundException {
        List<StudentDto> allStudents = studentService.getStudentsWithExams();
        return ResponseEntity.status(HttpStatus.OK).body(allStudents);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto) {
        Optional<StudentDto> updatedStudent = Optional.ofNullable(studentService.updateStudent(id, studentDto));
        return updatedStudent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();}
}
