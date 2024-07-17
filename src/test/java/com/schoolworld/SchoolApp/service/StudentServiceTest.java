package com.schoolworld.SchoolApp.service;

import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.dto.StudentDto;
import com.schoolworld.SchoolApp.exceptions.StudentNotFoundException;
import com.schoolworld.SchoolApp.exceptions.StudentWithSuchIdExists;
import com.schoolworld.SchoolApp.mappers.StudentMapper;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    @Mock
    private StudentRepo studentRepo;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;
    private StudentDto studentDto;
    private Student student;
    private StudentDto expectedStudentDto;
    private List<Student> students;
    private Student updatedStudent;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        //given
        studentDto = new StudentDto();
        studentDto.setName("Wojtek");
        studentDto.setEmail("wojtek12@op.pl");

        student = new Student();
        student.setId(1L);
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());

        updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setName("Adrian");
        updatedStudent.setEmail("adrian@op.pl");

        expectedStudentDto = new StudentDto();
        expectedStudentDto.setName(student.getName());
        expectedStudentDto.setEmail(student.getEmail());

        students = List.of(student);
    }

    @Test
    public void shouldSaveNewStudentSuccessfully() throws StudentWithSuchIdExists {
        // given
        when(studentMapper.toEntity(studentDto)).thenReturn(student);
        when(studentRepo.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(expectedStudentDto);

        // when
        StudentDto savedStudentDto = studentService.save(studentDto);

        // then
        assertNotNull(savedStudentDto);
        assertEquals(expectedStudentDto.getEmail(), savedStudentDto.getEmail());
        verify(studentMapper, times(1)).toEntity(studentDto);
        verify(studentRepo, times(1)).save(student);
        verify(studentMapper, times(1)).toDto(student);
    }

    @Test
    void shouldFindStudentById() throws StudentNotFoundException {
        //given
        when(studentRepo.findById(any(Long.class))).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);
        //when
        Optional<StudentDto> studentFoundById = studentService.findById(student.getId());
        //then
        assertNotNull(studentFoundById);
        assertEquals(studentFoundById.get().getName(), studentDto.getName());
        verify(studentRepo, times(1)).findById(student.getId());
        verify(studentMapper, times(1)).toDto(student);
    }

    @Test
    void shouldFindStudentByEmail() throws StudentNotFoundException {
        //given
        when(studentRepo.findByEmail(any(String.class))).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);
        //when
        Optional<StudentDto> studentFoundByEmail = studentService.findByEmail(student.getEmail());
        //then
        assertNotNull(studentFoundByEmail);
        assertEquals(studentFoundByEmail.get().getEmail(), studentDto.getEmail());
        verify(studentRepo, times(1)).findByEmail(student.getEmail());
        verify(studentMapper, times(1)).toDto(student);
    }

    @Test
    void shouldFindAllStudents() {
        //given
        when(studentRepo.findAll()).thenReturn(students);
        when(studentMapper.toDto(student)).thenReturn(studentDto);
        //when
        List<StudentDto> allStudents = studentService.findAll();
        //then
        assertNotNull(allStudents);
        assertEquals(allStudents.size(), 1);
        verify(studentRepo, times(1)).findAll();
        verify(studentMapper, times(1)).toDto(student);
    }

    @Test
    void shouldUpdateStudentWithGivenId() throws StudentNotFoundException {
        // given
        when(studentRepo.findById(student.getId())).thenReturn(Optional.of(updatedStudent));
        when(studentRepo.save(student)).thenReturn(updatedStudent);
        when(studentMapper.toDto(updatedStudent)).thenReturn(expectedStudentDto);

        // when
        Optional<StudentDto> updatedStudentDto = Optional.ofNullable(studentService.updateStudent(student.getId(), studentDto));

        // then
        assertTrue(updatedStudentDto.isPresent());
        assertEquals(expectedStudentDto.getEmail(), updatedStudentDto.get().getEmail());
        verify(studentRepo, times(1)).findById(student.getId());
        verify(studentRepo, times(1)).save(student);
        verify(studentMapper, times(1)).toDto(updatedStudent);
    }

    @Test
    void deleteById() throws StudentNotFoundException {
        //given
        when(studentRepo.findById(any(Long.class))).thenReturn(Optional.of(student));
        doNothing().when(studentRepo).deleteById(any(Long.class));
        //when
        studentService.deleteStudent(student.getId());
        //then
        verify(studentRepo, times(1)).findById(student.getId());
        verify(studentRepo, times(1)).deleteById(student.getId());

    }
}