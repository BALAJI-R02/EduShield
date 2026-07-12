package com.edushield.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edushield.backend.entity.AttendanceRecord;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByStudentIdOrderByYearAscMonthAsc(Long studentId);
}