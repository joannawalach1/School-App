package com.schoolworld.SchoolApp.service;

import com.schoolworld.SchoolApp.domain.Exam;
import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.Subject;
import com.schoolworld.SchoolApp.domain.dto.ExamDto;
import com.schoolworld.SchoolApp.exceptions.ExamWithSuchNameExistsException;
import com.schoolworld.SchoolApp.mappers.ExamMapper;
import com.schoolworld.SchoolApp.repository.ExamRepo;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepo examRepo;
    private final ExamMapper examMapper;
    private final StudentRepo studentRepo;
    private final SubjectRepo subjectRepo;
@Transactional
    public ExamDto save(ExamDto examDto) throws Exception {
            Exam exam = examMapper.toEntity(examDto);

            Student student = studentRepo.findById(examDto.getStudentId())
                    .orElseThrow(() -> new ExamWithSuchNameExistsException("Student not found with ID: " + examDto.getStudentId()));
            Subject subject = subjectRepo.findById(examDto.getSubjectId())
                    .orElseThrow(() -> new ExamWithSuchNameExistsException("Subject not found with ID: " + examDto.getSubjectId()));

            exam.setStudent(student);
            exam.setSubject(subject);
            exam = examRepo.save(exam);
            return examMapper.toDto(exam);
        }


    public ExamDto findById(Long id) {
        Exam foundExam = examRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Exam with id: " + id + " not found"));
        return examMapper.toDto(foundExam);
    }

    public List<ExamDto> findAllExams() {
        return examRepo.findAll().stream()
                .map(examMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteExam(Long id) {
        Exam examToDelete = examRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exam with id: " + id + " not found"));
        examRepo.deleteById(examToDelete.getId());
    }

    public ExamDto updateExam(Long id, ExamDto examDto) {
        Exam examToUpdate = examRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exam with id: " + id + " not found"));

        examToUpdate.setNameOfExam(examDto.getNameOfExam());
        examToUpdate.setDateOfExam(examDto.getDateOfExam());

        Exam savedExam = examRepo.save(examToUpdate);
        return examMapper.toDto(savedExam);
    }
}
