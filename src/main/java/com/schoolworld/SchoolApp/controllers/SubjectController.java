package com.schoolworld.SchoolApp.controllers;

import com.schoolworld.SchoolApp.domain.dto.SubjectDto;
import com.schoolworld.SchoolApp.exceptions.SubjectNotFoundException;
import com.schoolworld.SchoolApp.exceptions.SubjectWithSuchNameExistsException;
import com.schoolworld.SchoolApp.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

    @RestController
    @RequestMapping("/subject")
    public class SubjectController {
        private final SubjectService subjectService;

        public SubjectController(SubjectService subjectService) {
            this.subjectService = subjectService;
        }

        @PostMapping
        public ResponseEntity<SubjectDto> saveSubject(@RequestBody SubjectDto subjectDto) throws SubjectNotFoundException, SubjectWithSuchNameExistsException {
            SubjectDto createdSubject = subjectService.saveSubject(subjectDto);
            return ResponseEntity.status(HttpStatus.OK).body(createdSubject);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Optional<SubjectDto>> findById(@PathVariable Long id) throws SubjectNotFoundException {
            SubjectDto subjectDto = subjectService.findById(id);
            return  ResponseEntity.status(HttpStatus.OK).body(Optional.ofNullable(subjectDto));
        }

        @PutMapping("/{id}")
        public ResponseEntity<Optional<SubjectDto>> updateSubject(@PathVariable Long id, @RequestBody SubjectDto subjectDto) throws SubjectNotFoundException {
            Optional<SubjectDto> updatedSubject = Optional.ofNullable(subjectService.updateSubject(id, subjectDto));
            return ResponseEntity.status(HttpStatus.OK).body(updatedSubject);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteById(@PathVariable Long id) throws SubjectNotFoundException {
            subjectService.deleteSubject(id);
            return ResponseEntity.noContent().build();
        }
    }
