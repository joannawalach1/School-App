package com.schoolworld.SchoolApp.service;

import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.dto.StudentDto;
import com.schoolworld.SchoolApp.domain.dto.StudentRequestDto;
import com.schoolworld.SchoolApp.exceptions.StudentNotFoundException;
import com.schoolworld.SchoolApp.exceptions.StudentWithSuchIdExists;
import com.schoolworld.SchoolApp.mappers.StudentMapper;
import com.schoolworld.SchoolApp.repository.ExamRepo;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class StudentServiceTest {

    @Mock
    private StudentRepo studentRepo;
    @Mock
    private ExamRepo examRepo;
    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private StudentDto studentDto;
    private Student student;
    private StudentDto expectedStudentDto;
    private List<Student> students;
    private Student updatedStudent;
    private StudentRequestDto studentRequestDto;

    @BeforeAll
    public static void setUp() {
        PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
                .withDatabaseName("school1")
                .withUsername("postgres")
                .withPassword("666666");

        postgres.start();
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }

    @BeforeEach
    public void setUp1() {
        studentRequestDto = new StudentRequestDto();
        studentRequestDto.setName("Adrian");
        studentRequestDto.setEmail("adrian@op.pl");

        student = new Student();
        student.setId(1L);
        student.setName(studentRequestDto.getName());
        student.setEmail(studentRequestDto.getEmail());

        studentDto = new StudentDto();
        studentDto.setName(student.getName());
        studentDto.setEmail(student.getEmail());

        updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setName("Adrian");
        updatedStudent.setEmail("adrian@op.pl");

        expectedStudentDto = new StudentDto();
        expectedStudentDto.setName(updatedStudent.getName());
        expectedStudentDto.setEmail(updatedStudent.getEmail());

        students = List.of(student);
    }

    @Test
    public void shouldSaveNewStudentSuccessfully() throws StudentWithSuchIdExists {
        when(studentRepo.save(any(Student.class))).thenReturn(student);

        StudentDto savedStudentDto = studentService.save(studentRequestDto);

        assertNotNull(savedStudentDto);
        assertEquals(studentDto.getEmail(), savedStudentDto.getEmail());
        assertEquals(studentDto.getName(), savedStudentDto.getName());
        verify(studentRepo, times(1)).save(any(Student.class));
    }

    @Test
    void shouldFindStudentById() throws StudentNotFoundException {
        when(studentRepo.findById(any(Long.class))).thenReturn(Optional.of(student));

        Optional<StudentDto> studentFoundById = studentService.findById(student.getId());

        assertTrue(studentFoundById.isPresent());
        assertEquals(studentDto.getName(), studentFoundById.get().getName());
        verify(studentRepo, times(1)).findById(student.getId());
    }

    @Test
    void shouldFindStudentByEmail() throws StudentNotFoundException {
        when(studentRepo.findByEmail(any(String.class))).thenReturn(Optional.of(student));

        Optional<StudentDto> studentFoundByEmail = studentService.findByEmail(student.getEmail());

        assertTrue(studentFoundByEmail.isPresent());
        assertEquals(studentDto.getEmail(), studentFoundByEmail.get().getEmail());
        verify(studentRepo, times(1)).findByEmail(student.getEmail());
    }
//TODO test nie przechodzi examRepo null
    @Test
    void shouldFindAllStudents() {
        when(studentRepo.findAll()).thenReturn(students);

        List<StudentDto> allStudents = studentService.getStudentsWithExams();

        assertNotNull(allStudents);
        assertEquals(1, allStudents.size());
        verify(studentRepo, times(1)).findAll();
    }

    @Test
    void shouldUpdateStudentWithGivenId() throws StudentNotFoundException {
        when(studentRepo.findById(any(Long.class))).thenReturn(Optional.of(updatedStudent));
        when(studentRepo.save(any(Student.class))).thenReturn(updatedStudent);

        Optional<StudentDto> updatedStudentDto = Optional.ofNullable(studentService.updateStudent(student.getId(), studentDto));

        assertTrue(updatedStudentDto.isPresent());
        assertEquals(expectedStudentDto.getEmail(), updatedStudentDto.get().getEmail());
        verify(studentRepo, times(1)).findById(student.getId());
        verify(studentRepo, times(1)).save(any(Student.class));
    }

    @Test
    void deleteById() throws StudentNotFoundException {
        when(studentRepo.findById(any(Long.class))).thenReturn(Optional.of(student));
        doNothing().when(studentRepo).deleteById(any(Long.class));

        studentService.deleteStudent(student.getId());

        verify(studentRepo, times(1)).findById(student.getId());
        verify(studentRepo, times(1)).deleteById(student.getId());
    }
}
