package com.schoolworld.SchoolApp.repository;

import com.schoolworld.SchoolApp.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
}
