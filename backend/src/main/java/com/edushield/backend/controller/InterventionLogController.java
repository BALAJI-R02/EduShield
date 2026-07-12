package com.edushield.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edushield.backend.dto.InterventionLogRequest;
import com.edushield.backend.dto.InterventionLogResponse;
import com.edushield.backend.service.InterventionLogService;

@RestController
@RequestMapping("/api/interventions")
public class InterventionLogController {

    @Autowired
    private InterventionLogService interventionLogService;

    @PostMapping
    public ResponseEntity<InterventionLogResponse> addLog(@RequestBody InterventionLogRequest request) {
        return ResponseEntity.ok(interventionLogService.addLog(request));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<InterventionLogResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(interventionLogService.getLogsByStudent(studentId));
    }
}