package com.schoolworld.SchoolApp.service;

import com.github.dockerjava.api.exception.NotFoundException;
import com.schoolworld.SchoolApp.domain.Exam;
import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.Subject;
import com.schoolworld.SchoolApp.domain.dto.ExamDto;
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
        Exam entity = examMapper.toEntity(examDto);
        Student student = studentRepo.findById(examDto.getStudentId()).orElseThrow(() -> new NotFoundException("Student with id: " + examDto.getStudentId() + "doesn't exist"));
        Subject subject = subjectRepo.findById(examDto.getSubjectId()).orElseThrow(()-> new NotFoundException("Subject with id: " + examDto.getSubjectId() + "doesn't exist"));
        entity.setNameOfExam(examDto.getNameOfExam());
        entity.setDateOfExam(examDto.getDateOfExam());
        entity.setStudent(student);
        entity.setSubject(subject);
        Exam savedExam = examRepo.save(entity);
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
