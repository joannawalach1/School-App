package com.schoolworld.SchoolApp.service;

import com.schoolworld.SchoolApp.domain.Subject;
import com.schoolworld.SchoolApp.domain.dto.SubjectDto;
import com.schoolworld.SchoolApp.exceptions.SubjectWithSuchNameExistsException;
import com.schoolworld.SchoolApp.exceptions.SubjectsNotFoundException;
import com.schoolworld.SchoolApp.mappers.SubjectMapper;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepo subjectRepo;
    private final SubjectMapper subjectMapper;

    public SubjectDto saveSubject(SubjectDto subjectDto) throws SubjectWithSuchNameExistsException {
        if (subjectRepo.findByName(subjectDto.getName()).isPresent()) {
            throw new SubjectWithSuchNameExistsException("Subject with such name already exists.");
        }

        Subject subject = subjectMapper.toEntity(subjectDto);
        Subject savedSubject = subjectRepo.save(subject);

        return subjectMapper.toDto(savedSubject);
    }

    public SubjectDto findById(Long id) {
        Subject foundSubject = subjectRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Exam with id: " + id + " not found"));
        return subjectMapper.toDto(foundSubject);
    }

    public List<SubjectDto> findAllExams() throws SubjectsNotFoundException {
        List<Subject> subjects = subjectRepo.findAll();
        if (subjects.isEmpty()){
            throw new SubjectsNotFoundException("No subjects in database");
        }
        return subjectRepo.findAll().stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteSubject(Long id) {
        Subject subjectToDelete = subjectRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject with id: " + id + " not found"));
        subjectRepo.deleteById(subjectToDelete.getId());
    }

    public SubjectDto updateSubject(Long id, SubjectDto subjectDto) {
        Subject existingSubject = subjectRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject with id: " + id + " not found"));

        Subject subjectToUpdate = subjectMapper.toEntity(subjectDto);
        subjectToUpdate.setId(existingSubject.getId());

        Subject updatedSubject = subjectRepo.save(subjectToUpdate);
        return subjectMapper.toDto(updatedSubject);
    }
}
