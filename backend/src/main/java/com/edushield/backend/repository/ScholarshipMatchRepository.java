package com.edushield.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edushield.backend.entity.ScholarshipMatch;

public interface ScholarshipMatchRepository extends JpaRepository<ScholarshipMatch, Long> {
    List<ScholarshipMatch> findByStudentId(Long studentId);
    List<ScholarshipMatch> findByScholarshipId(Long scholarshipId);
}