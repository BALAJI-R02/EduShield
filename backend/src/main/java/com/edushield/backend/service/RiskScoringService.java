package com.edushield.backend.service;

import com.edushield.backend.dto.RiskScoreResponse;
import com.edushield.backend.entity.*;
import com.edushield.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RiskScoringService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRecordRepository attendanceRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private FeeStatusRepository feeStatusRepository;

    @Autowired
    private RiskScoreRepository riskScoreRepository;

    private static final double ATTENDANCE_WEIGHT = 0.40;
    private static final double MARKS_WEIGHT = 0.35;
    private static final double FEE_WEIGHT = 0.25;

    private static final double ATTENDANCE_THRESHOLD = 75.0;
    private static final double MARKS_THRESHOLD = 50.0;

    public RiskScoreResponse computeRisk(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        // 1. Attendance risk factor
        List<AttendanceRecord> attendanceRecords = attendanceRepository.findByStudentIdOrderByYearAscMonthAsc(studentId);
        double attendancePct = calculateAverageAttendance(attendanceRecords);
        double attendanceRiskFactor = attendancePct < ATTENDANCE_THRESHOLD
                ? (ATTENDANCE_THRESHOLD - attendancePct) / ATTENDANCE_THRESHOLD
                : 0.0;

        // 2. Marks risk factor
        List<Marks> marksList = marksRepository.findByStudentIdOrderByAssessmentDateAsc(studentId);
        double marksAvg = calculateAverageMarks(marksList);
        double marksRiskFactor = marksAvg < MARKS_THRESHOLD
                ? (MARKS_THRESHOLD - marksAvg) / MARKS_THRESHOLD
                : 0.0;

        // 3. Fee risk factor
        List<FeeStatus> feeStatuses = feeStatusRepository.findByStudentIdAndPaidFalse(studentId);
        boolean feeOverdue = feeStatuses.stream()
                .anyMatch(fee -> fee.getDueDate() != null && fee.getDueDate().isBefore(LocalDate.now()));
        double feeRiskFactor = feeOverdue ? 1.0 : 0.0;

        // Weighted final score (0 to 1 scale, then converted to 0-100)
        double finalScore = (ATTENDANCE_WEIGHT * attendanceRiskFactor)
                + (MARKS_WEIGHT * marksRiskFactor)
                + (FEE_WEIGHT * feeRiskFactor);

        double riskScorePercent = Math.round(finalScore * 10000.0) / 100.0; // 0-100 scale

        RiskLevel riskLevel;
        if (riskScorePercent >= 60) {
            riskLevel = RiskLevel.HIGH;
        } else if (riskScorePercent >= 30) {
            riskLevel = RiskLevel.MEDIUM;
        } else {
            riskLevel = RiskLevel.LOW;
        }

        // Save this computation as a historical record
        RiskScore riskScore = new RiskScore();
        riskScore.setStudent(student);
        riskScore.setAttendancePct(attendancePct);
        riskScore.setMarksAvg(marksAvg);
        riskScore.setFeeOverdue(feeOverdue);
        riskScore.setRiskScoreValue(riskScorePercent);
        riskScore.setRiskLevel(riskLevel);
        riskScore.setComputedAt(LocalDateTime.now());
        riskScoreRepository.save(riskScore);

        return new RiskScoreResponse(
                student.getId(),
                student.getName(),
                attendancePct,
                marksAvg,
                feeOverdue,
                riskScorePercent,
                riskLevel,
                riskScore.getComputedAt()
        );
    }

    public List<RiskScoreResponse> getRiskHistory(Long studentId) {
        return riskScoreRepository.findByStudentIdOrderByComputedAtAsc(studentId)
                .stream()
                .map(rs -> new RiskScoreResponse(
                        rs.getStudent().getId(),
                        rs.getStudent().getName(),
                        rs.getAttendancePct(),
                        rs.getMarksAvg(),
                        rs.getFeeOverdue(),
                        rs.getRiskScoreValue(),
                        rs.getRiskLevel(),
                        rs.getComputedAt()
                ))
                .toList();
    }

    private double calculateAverageAttendance(List<AttendanceRecord> records) {
        if (records.isEmpty()) return 100.0; // no data = assume fine, avoid false-flagging new students
        double totalPresent = records.stream().mapToInt(AttendanceRecord::getPresentDays).sum();
        double totalDays = records.stream().mapToInt(AttendanceRecord::getTotalDays).sum();
        return totalDays > 0 ? Math.round((totalPresent / totalDays) * 10000.0) / 100.0 : 100.0;
    }

    private double calculateAverageMarks(List<Marks> marksList) {
        if (marksList.isEmpty()) return 100.0; // no data = assume fine
        double avgPct = marksList.stream()
                .mapToDouble(m -> (m.getScore() / m.getMaxScore()) * 100.0)
                .average()
                .orElse(100.0);
        return Math.round(avgPct * 100.0) / 100.0;
    }
}