package com.schoolworld.SchoolApp.controllers;

import com.schoolworld.SchoolApp.domain.dto.ExamDto;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import com.schoolworld.SchoolApp.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ExamDto> findById(@PathVariable Long id) {
        ExamDto examDto = examService.findById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(examDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamDto> updateExam(@PathVariable Long id, @RequestBody ExamDto examDto) {
        ExamDto updatedExam = examService.updateExam(id, examDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedExam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }
}
