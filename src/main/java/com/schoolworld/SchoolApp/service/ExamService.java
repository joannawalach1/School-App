package com.schoolworld.SchoolApp.service;

import com.schoolworld.SchoolApp.domain.Exam;
import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.Subject;
import com.schoolworld.SchoolApp.domain.dto.ExamDto;
import com.schoolworld.SchoolApp.exceptions.ExamWithSuchNameExistsException;
import com.schoolworld.SchoolApp.exceptions.ExamsNotFoundException;
import com.schoolworld.SchoolApp.mappers.ExamMapper;
import com.schoolworld.SchoolApp.repository.ExamRepo;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    if (examDto == null) {
        throw new IllegalArgumentException("ExamDto must not be null");
    }
   Optional<Exam> foundExam = examRepo.findByNameOfExam(examDto.getNameOfExam());
   if (foundExam.isPresent()) {
       throw new ExamWithSuchNameExistsException("Exam with such name: " + examDto.getNameOfExam() + "exists");
   }
    Optional<Student> optionalStudent = examDto.getStudentId() != null ?
            studentRepo.findById(examDto.getStudentId()) :
            Optional.empty();

    Optional<Subject> optionalSubject = subjectRepo.findById(examDto.getSubjectId());

    if (optionalSubject.isEmpty()) {
        throw new RuntimeException("Subject not found");
    }

    Subject subject = optionalSubject.get();
    Student student = optionalStudent.orElse(null);

    Exam exam = examMapper.toEntity(examDto);
    exam.setSubject(subject);
    exam.setStudent(student);
    Exam savedExam = examRepo.save(exam);
    return examMapper.toDto(savedExam);
        }


    public ExamDto findById(Long id) {
        Exam foundExam = examRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Exam with id: " + id + " not found"));
        return examMapper.toDto(foundExam);
    }

    public List<ExamDto> findAllExams() throws ExamsNotFoundException {
        List<Exam> exams = examRepo.findAll();
        if (exams.isEmpty()) {
            throw new ExamsNotFoundException("No exams in database");
        }
        return examRepo.findAll().stream()
                .map(examMapper::toDto)
                .collect(Collectors.toList());
    }
    public void deleteExam(Long id) {
            Exam examToDelete = examRepo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Exam with id: " + id + " not found"));

            Student student = examToDelete.getStudent();
            if (student != null) {
                student.getExams().remove(examToDelete);
                studentRepo.save(student);
            }

            Subject subject = examToDelete.getSubject();
            if (subject != null) {
                subject.getExams().remove(examToDelete);
                subjectRepo.save(subject);
            }

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
