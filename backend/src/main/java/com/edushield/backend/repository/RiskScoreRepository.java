package com.edushield.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edushield.backend.entity.RiskScore;

public interface RiskScoreRepository extends JpaRepository<RiskScore, Long> {
    List<RiskScore> findByStudentIdOrderByComputedAtAsc(Long studentId);
}