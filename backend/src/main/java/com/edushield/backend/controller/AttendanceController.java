package com.edushield.backend.controller;

import com.edushield.backend.dto.AttendanceRequest;
import com.edushield.backend.dto.AttendanceResponse;
import com.edushield.backend.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponse> addAttendance(@RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.addAttendance(request));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId));
    }
}