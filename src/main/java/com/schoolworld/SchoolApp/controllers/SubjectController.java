package com.schoolworld.SchoolApp.controllers;

import com.schoolworld.SchoolApp.domain.dto.SubjectDto;
import com.schoolworld.SchoolApp.exceptions.SubjectWithSuchNameExistsException;
import com.schoolworld.SchoolApp.exceptions.SubjectsNotFoundException;
import com.schoolworld.SchoolApp.service.SubjectService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

    @RestController
    @RequestMapping("/subject")
    public class SubjectController {
        private final SubjectService subjectService;

        public SubjectController(SubjectService subjectService) {
            this.subjectService = subjectService;
        }
        @Transactional
        @PostMapping
        public ResponseEntity<SubjectDto> saveSubject(@RequestBody SubjectDto subjectDto) throws SubjectWithSuchNameExistsException {
            SubjectDto createdSubject = subjectService.saveSubject(subjectDto);
            return ResponseEntity.status(HttpStatus.OK).body(createdSubject);
        }

        @GetMapping("/find/{id}")
        public ResponseEntity<Optional<SubjectDto>> findById(@PathVariable Long id) {
            SubjectDto subjectDto = subjectService.findById(id);
            return  ResponseEntity.status(HttpStatus.OK).body(Optional.ofNullable(subjectDto));
        }

        @GetMapping("/getSubjects")
        public ResponseEntity<List<SubjectDto>> findAllSubjects() throws SubjectsNotFoundException {
            List<SubjectDto> subjects = subjectService.findAllSubjects();
            return  ResponseEntity.status(HttpStatus.OK).body(subjects);
        }

        @PutMapping("/update/{id}")
        public ResponseEntity<Optional<SubjectDto>> updateSubject(@PathVariable Long id, @RequestBody SubjectDto subjectDto) {
            Optional<SubjectDto> updatedSubject = Optional.ofNullable(subjectService.updateSubject(id, subjectDto));
            return ResponseEntity.status(HttpStatus.OK).body(updatedSubject);
        }
        @Transactional
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<Void> deleteById(@PathVariable Long id) {
            subjectService.deleteSubject(id);
            return ResponseEntity.noContent().build();
        }
    }
