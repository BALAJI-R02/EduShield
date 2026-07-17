package com.edushield.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edushield.backend.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserId(Long userId);
    List<Student> findByMentorId(Long mentorId);
    boolean existsByRollNo(String rollNo);
    Optional<Student> findByRollNo(String rollNo);
}