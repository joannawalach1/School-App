package com.schoolworld.SchoolApp.repository;

import com.schoolworld.SchoolApp.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    @Query("SELECT s FROM Student s JOIN FETCH s.exams")
    List<Student> findAllWithExams();
}
