package com.edushield.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edushield.backend.entity.Marks;

public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudentIdOrderByAssessmentDateAsc(Long studentId);
}