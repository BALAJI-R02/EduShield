package com.edushield.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edushield.backend.entity.FeeStatus;

public interface FeeStatusRepository extends JpaRepository<FeeStatus, Long> {
    List<FeeStatus> findByStudentId(Long studentId);
    List<FeeStatus> findByStudentIdAndPaidFalse(Long studentId);
}