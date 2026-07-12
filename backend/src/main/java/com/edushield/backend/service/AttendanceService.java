package com.edushield.backend.service;

import com.edushield.backend.dto.AttendanceRequest;
import com.edushield.backend.dto.AttendanceResponse;
import com.edushield.backend.entity.AttendanceRecord;
import com.edushield.backend.entity.Student;
import com.edushield.backend.repository.AttendanceRecordRepository;
import com.edushield.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRecordRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    public AttendanceResponse addAttendance(AttendanceRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        AttendanceRecord record = new AttendanceRecord();
        record.setStudent(student);
        record.setMonth(request.getMonth());
        record.setYear(request.getYear());
        record.setTotalDays(request.getTotalDays());
        record.setPresentDays(request.getPresentDays());

        AttendanceRecord saved = attendanceRepository.save(record);
        return mapToResponse(saved);
    }

    public List<AttendanceResponse> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentIdOrderByYearAscMonthAsc(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AttendanceResponse mapToResponse(AttendanceRecord record) {
        double percentage = record.getTotalDays() > 0
                ? (record.getPresentDays() * 100.0) / record.getTotalDays()
                : 0.0;

        return new AttendanceResponse(
                record.getId(),
                record.getStudent().getId(),
                record.getMonth(),
                record.getYear(),
                record.getTotalDays(),
                record.getPresentDays(),
                Math.round(percentage * 100.0) / 100.0
        );
    }
}