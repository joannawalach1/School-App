package com.schoolworld.SchoolApp.service;


import com.schoolworld.SchoolApp.domain.Exam;
import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.Subject;
import com.schoolworld.SchoolApp.domain.dto.ExamDto;
import com.schoolworld.SchoolApp.domain.dto.StudentDto;
import com.schoolworld.SchoolApp.domain.dto.SubjectDto;
import com.schoolworld.SchoolApp.exceptions.ExamNotFoundException;
import com.schoolworld.SchoolApp.exceptions.ExamWithSuchNameExistsException;
import com.schoolworld.SchoolApp.mappers.ExamMapper;
import com.schoolworld.SchoolApp.repository.ExamRepo;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


class ExamServiceTest {

    @Mock
    private ExamRepo examRepo;
    @Mock
    private StudentRepo studentRepo;
    @Mock
    private ExamMapper examMapper;
    @Mock
    private SubjectRepo subjectRepo;
    @InjectMocks
    private ExamService examService;
    private ExamDto examDto;
    private Exam exam;
    private ExamDto expectedExamDto;
    private List<Exam> exams;
    private Exam updatedExam;

    private SubjectDto subjectDto;
    private StudentDto studentDto;
    private Student student;
    private Subject subject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // given
        examDto = new ExamDto();
        examDto.setNameOfExam("A1");
        examDto.setDateOfExam(LocalDateTime.of(2024, 5, 24, 3, 10));
        examDto.setStudentId(1L);
        examDto.setSubjectId(1L);

        exam = new Exam();
        exam.setId(1L);
        exam.setNameOfExam(examDto.getNameOfExam());
        exam.setDateOfExam(examDto.getDateOfExam());
        exam.setStudent(new Student(examDto.getStudentId()));
        exam.setSubject(new Subject(examDto.getSubjectId()));

        studentDto = new StudentDto();
        studentDto.setName("Wojtek");
        studentDto.setEmail("wojtek12@op.pl");

        student = new Student();
        student.setId(1L);
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());


        subjectDto = new SubjectDto();
        subjectDto.setName("Math");

        subject = new Subject();
        subject.setId(1L);
        subject.setName(subjectDto.getName());


        updatedExam = new Exam();
        updatedExam.setId(1L);
        updatedExam.setNameOfExam(examDto.getNameOfExam());
        updatedExam.setDateOfExam(examDto.getDateOfExam());
        updatedExam.setStudent(new Student(examDto.getStudentId()));
        updatedExam.setSubject(new Subject(examDto.getSubjectId()));

        expectedExamDto = new ExamDto();
        expectedExamDto.setNameOfExam(exam.getNameOfExam());
        expectedExamDto.setDateOfExam(exam.getDateOfExam());
        expectedExamDto.setStudentId(examDto.getStudentId());
        expectedExamDto.setSubjectId(examDto.getSubjectId());

        exams = List.of(exam);
    }

    @Test
    void shouldSaveExam() throws ExamWithSuchNameExistsException {
        when(examRepo.findByNameOfExam(anyString())).thenReturn(false);
        when(studentRepo.findById(anyLong())).thenReturn(Optional.of(student));
        when(subjectRepo.findById(anyLong())).thenReturn(Optional.of(subject));
        when(examMapper.toEntity(any(ExamDto.class))).thenReturn(exam);
        when(examRepo.save(any(Exam.class))).thenReturn(exam);
        when(examMapper.toDto(any(Exam.class))).thenReturn(examDto);

        ExamDto result = examService.save(examDto);

        assertNotNull(result);
        assertEquals(examDto.getNameOfExam(), result.getNameOfExam());

        verify(examRepo, times(1)).findByNameOfExam(anyString());
        verify(studentRepo, times(1)).findById(anyLong());
        verify(subjectRepo, times(1)).findById(anyLong());
        verify(examRepo, times(1)).save(any(Exam.class));
        verify(examMapper, times(1)).toDto(any(Exam.class));
    }

    @Test
    void shouldFindExamById() throws ExamNotFoundException {
        // given
        when(examRepo.findById(any())).thenReturn(Optional.of(exam));
        when(examMapper.toDto(exam)).thenReturn(examDto);

        // when
        Optional<ExamDto> examFoundById = Optional.ofNullable(examService.findById(exam.getId()));

        // then
        assertTrue(examFoundById.isPresent());
        assertEquals(examDto.getNameOfExam(), examFoundById.get().getNameOfExam());
        verify(examRepo, times(1)).findById(exam.getId());
        verify(examMapper, times(1)).toDto(exam);
    }

    @Test
    void updateExamWithGivenId() throws ExamNotFoundException {
        // given
        when(examRepo.findById(exam.getId())).thenReturn(Optional.of(updatedExam));
        when(examRepo.save(exam)).thenReturn(updatedExam);
        when(examMapper.toDto(updatedExam)).thenReturn(expectedExamDto);

        // when
        Optional<ExamDto> updatedExamDto = Optional.ofNullable(examService.updateExam(exam.getId(), examDto));

        // then
        assertTrue(updatedExamDto.isPresent());
        assertEquals(expectedExamDto.getNameOfExam(), updatedExamDto.get().getNameOfExam());
        verify(examRepo, times(1)).findById(exam.getId());
        verify(examRepo, times(1)).save(exam);
        verify(examMapper, times(1)).toDto(updatedExam);
    }

    @Test
    void shouldDeleteExamById() throws ExamNotFoundException {
        // given
        when(examRepo.findById(anyLong())).thenReturn(Optional.of(exam));
        doNothing().when(examRepo).deleteById(anyLong());

        // when
        examService.deleteExam(exam.getId());

        // then
        verify(examRepo, times(1)).findById(exam.getId());
        verify(examRepo, times(1)).deleteById(exam.getId());
    }
}
