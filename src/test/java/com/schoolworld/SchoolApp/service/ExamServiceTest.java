package com.schoolworld.SchoolApp.service;

import com.schoolworld.SchoolApp.domain.Exam;
import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.Subject;
import com.schoolworld.SchoolApp.domain.dto.ExamDto;
import com.schoolworld.SchoolApp.mappers.ExamMapper;
import com.schoolworld.SchoolApp.repository.ExamRepo;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ExamServiceTest {

    @Mock
    private ExamRepo examRepo;

    @Mock
    private StudentRepo studentRepo;

    @Mock
    private SubjectRepo subjectRepo;

    @Mock
    private ExamMapper examMapper;

    @InjectMocks
    private ExamService examService;

    private ExamDto examDto;
    private Exam exam;
    private ExamDto expectedExamDto;
    private Student student;
    private Subject subject;

    @BeforeEach
    void setUp() {
        examDto = new ExamDto();
        examDto.setNameOfExam("A1");
        examDto.setDateOfExam(LocalDateTime.of(2024, 5, 24, 3, 10));
        examDto.setStudentId(1L);
        examDto.setSubjectId(1L);

        exam = new Exam();
        exam.setId(1L);
        exam.setNameOfExam(examDto.getNameOfExam());
        exam.setDateOfExam(examDto.getDateOfExam());

        subject = new Subject();
        subject.setId(1L); // Poprawiono ID, aby pasowało do testów
        subject.setName("Math");

        student = new Student();
        student.setId(1L);
        student.setName("Wojtek");
        student.setEmail("wojtek12@op.pl");

        expectedExamDto = new ExamDto();
        expectedExamDto.setNameOfExam(exam.getNameOfExam());
        expectedExamDto.setDateOfExam(exam.getDateOfExam());
        expectedExamDto.setStudentId(examDto.getStudentId());
        expectedExamDto.setSubjectId(examDto.getSubjectId());
    }

    @Test
    void shouldSaveExamSuccessfully() throws Exception {
        // given
        when(examMapper.toEntity(examDto)).thenReturn(exam);
        when(subjectRepo.findById(examDto.getSubjectId())).thenReturn(Optional.of(subject));
        when(studentRepo.findById(examDto.getStudentId())).thenReturn(Optional.of(student));
        when(examRepo.save(exam)).thenReturn(exam);
        when(examMapper.toDto(exam)).thenReturn(expectedExamDto);

        // when
        ExamDto savedExamDto = examService.save(examDto);

        // then
        assertNotNull(savedExamDto);
        assertEquals(expectedExamDto.getNameOfExam(), savedExamDto.getNameOfExam());
        verify(examMapper, times(1)).toEntity(examDto);
        verify(subjectRepo, times(1)).findById(examDto.getSubjectId());
        verify(studentRepo, times(1)).findById(examDto.getStudentId());
        verify(examRepo, times(1)).save(exam);
        verify(examMapper, times(1)).toDto(exam);
    }

    @Test
    void shouldFindExamById() {
        // given
        when(examRepo.findById(exam.getId())).thenReturn(Optional.of(exam));
        when(examMapper.toDto(exam)).thenReturn(expectedExamDto);

        // when
        ExamDto foundExamDto = examService.findById(exam.getId());

        // then
        assertNotNull(foundExamDto);
        assertEquals(expectedExamDto.getNameOfExam(), foundExamDto.getNameOfExam());
        verify(examRepo, times(1)).findById(exam.getId());
        verify(examMapper, times(1)).toDto(exam);
    }

    @Test
    void shouldThrowExceptionWhenExamNotFoundById() {
        // given
        when(examRepo.findById(exam.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> examService.findById(exam.getId()));
        verify(examRepo, times(1)).findById(exam.getId());
    }

    @Test
    void shouldFindAllExams() {
        // given
        when(examRepo.findAll()).thenReturn(List.of(exam));
        when(examMapper.toDto(exam)).thenReturn(expectedExamDto);

        // when
        List<ExamDto> examDtos = examService.findAllExams();

        // then
        assertNotNull(examDtos);
        assertFalse(examDtos.isEmpty());
        assertEquals(1, examDtos.size());
        assertEquals(expectedExamDto.getNameOfExam(), examDtos.get(0).getNameOfExam());
        verify(examRepo, times(1)).findAll();
        verify(examMapper, times(1)).toDto(exam);
    }

    @Test
    void shouldDeleteExamById() {
        // given
        when(examRepo.findById(exam.getId())).thenReturn(Optional.of(exam));
        doNothing().when(examRepo).deleteById(exam.getId());

        // when
        examService.deleteExam(exam.getId());

        // then
        verify(examRepo, times(1)).findById(exam.getId());
        verify(examRepo, times(1)).deleteById(exam.getId());
    }

    @Test
    void shouldThrowExceptionWhenDeleteExamNotFound() {
        // given
        when(examRepo.findById(exam.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> examService.deleteExam(exam.getId()));
        verify(examRepo, times(1)).findById(exam.getId());
    }

    @Test
    void shouldUpdateExamSuccessfully() {
        // given
        when(examRepo.findById(exam.getId())).thenReturn(Optional.of(exam));
        when(examMapper.toEntity(examDto)).thenReturn(exam);
        when(examRepo.save(exam)).thenReturn(exam);
        when(examMapper.toDto(exam)).thenReturn(expectedExamDto);

        // when
        ExamDto updatedExamDto = examService.updateExam(exam.getId(), examDto);

        // then
        assertNotNull(updatedExamDto);
        assertEquals(expectedExamDto.getNameOfExam(), updatedExamDto.getNameOfExam());
        verify(examRepo, times(1)).findById(exam.getId());
        verify(examRepo, times(1)).save(exam);
        verify(examMapper, times(1)).toDto(exam);
    }

    @Test
    void shouldThrowExceptionWhenUpdateExamNotFound() {
        // given
        when(examRepo.findById(exam.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> examService.updateExam(exam.getId(), examDto));
        verify(examRepo, times(1)).findById(exam.getId());
    }
}
