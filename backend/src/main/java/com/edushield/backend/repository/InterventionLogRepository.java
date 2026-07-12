package com.edushield.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edushield.backend.entity.InterventionLog;

public interface InterventionLogRepository extends JpaRepository<InterventionLog, Long> {
    List<InterventionLog> findByStudentIdOrderByLogDateDesc(Long studentId);
}