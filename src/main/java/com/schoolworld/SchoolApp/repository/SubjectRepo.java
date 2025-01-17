package com.schoolworld.SchoolApp.repository;

import com.schoolworld.SchoolApp.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Long> {
    Optional<Subject> findByName(String name);
}
