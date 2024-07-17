package com.schoolworld.SchoolApp.service;

import com.schoolworld.SchoolApp.domain.Subject;
import com.schoolworld.SchoolApp.domain.dto.SubjectDto;
import com.schoolworld.SchoolApp.exceptions.SubjectNotFoundException;
import com.schoolworld.SchoolApp.exceptions.SubjectWithSuchNameExistsException;
import com.schoolworld.SchoolApp.mappers.SubjectMapper;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SubjectServiceTest {

    @Mock
    private SubjectRepo subjectRepo;

    @Mock
    private SubjectMapper subjectMapper;

    @InjectMocks
    private SubjectService subjectService;

    private SubjectDto subjectDto;
    private Subject subject;
    private SubjectDto expectedSubjectDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        subjectDto = new SubjectDto();
        subjectDto.setName("Math");

        subject = new Subject();
        subject.setId(1L);
        subject.setName(subjectDto.getName());

        expectedSubjectDto = new SubjectDto();
        expectedSubjectDto.setName("Math");
    }

    @Test
    void save() throws SubjectWithSuchNameExistsException {
        // given
        when(subjectMapper.toEntity(subjectDto)).thenReturn(subject);
        when(subjectRepo.save(subject)).thenReturn(subject);
        when(subjectMapper.toDto(subject)).thenReturn(expectedSubjectDto);

        // when
        SubjectDto savedSubjectDto = subjectService.saveSubject(subjectDto);

        // then
        assertNotNull(savedSubjectDto);
        assertEquals(expectedSubjectDto.getName(), savedSubjectDto.getName());
        verify(subjectMapper, times(1)).toEntity(subjectDto);
        verify(subjectRepo, times(1)).save(subject);
        verify(subjectMapper, times(1)).toDto(subject);
    }

    @Test
    void findById() throws SubjectNotFoundException {
        // given
        when(subjectRepo.findById(subject.getId())).thenReturn(Optional.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        // when
        SubjectDto subjectFoundById = subjectService.findById(subject.getId());

        // then
        assertNotNull(subjectFoundById);
        assertEquals(subjectDto.getName(), subjectFoundById.getName());
        verify(subjectRepo, times(1)).findById(subject.getId());
        verify(subjectMapper, times(1)).toDto(subject);
    }

    @Test
    void update() throws SubjectNotFoundException {
        // given
        when(subjectRepo.findById(subject.getId())).thenReturn(Optional.of(subject));
        when(subjectRepo.save(subject)).thenReturn(subject);
        when(subjectMapper.toDto(subject)).thenReturn(expectedSubjectDto);

        // when
        SubjectDto updatedSubjectDto = subjectService.updateSubject(subject.getId(), subjectDto);

        // then
        assertNotNull(updatedSubjectDto);
        assertEquals(expectedSubjectDto.getName(), updatedSubjectDto.getName());
        verify(subjectRepo, times(1)).findById(subject.getId());
        verify(subjectRepo, times(1)).save(subject);
        verify(subjectMapper, times(1)).toDto(subject);
    }

    @Test
    void deleteById() throws SubjectNotFoundException {
        // given
        when(subjectRepo.findById(subject.getId())).thenReturn(Optional.of(subject));
        doNothing().when(subjectRepo).deleteById(subject.getId());

        // when
        subjectService.deleteSubject(subject.getId());

        // then
        verify(subjectRepo, times(1)).findById(subject.getId());
        verify(subjectRepo, times(1)).deleteById(subject.getId());
    }
}