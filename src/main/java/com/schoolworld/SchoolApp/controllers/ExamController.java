package com.schoolworld.SchoolApp.controllers;

import com.schoolworld.SchoolApp.domain.dto.ExamDto;
import com.schoolworld.SchoolApp.exceptions.ExamNotFoundException;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import com.schoolworld.SchoolApp.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/exam")
public class ExamController {
    private final ExamService examService;
    private final SubjectRepo subjectRepo;

    public ExamController(ExamService ExamService, SubjectRepo subjectRepo) {
        this.examService = ExamService;
        this.subjectRepo = subjectRepo;
    }

    @PostMapping
    public ResponseEntity<ExamDto> saveExam(@RequestBody ExamDto examDto) throws Exception {
        ExamDto createdExam = examService.save(examDto);
        return ResponseEntity.status(HttpStatus.OK).body(createdExam);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ExamDto>> findById(@PathVariable Long id) {
        Optional<ExamDto> examDto = Optional.ofNullable(examService.findById(id));
        return  ResponseEntity.status(HttpStatus.OK).body(examDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<ExamDto>> updateExam(@PathVariable Long id, @RequestBody ExamDto examDto) {
        Optional<ExamDto> updatedExam = Optional.ofNullable(examService.updateExam(id, examDto));
        return ResponseEntity.status(HttpStatus.OK).body(updatedExam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }
}
