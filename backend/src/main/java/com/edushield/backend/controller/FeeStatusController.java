package com.edushield.backend.controller;

import com.edushield.backend.dto.FeeStatusRequest;
import com.edushield.backend.dto.FeeStatusResponse;
import com.edushield.backend.service.FeeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fees")
public class FeeStatusController {

    @Autowired
    private FeeStatusService feeStatusService;

    @PostMapping
    public ResponseEntity<FeeStatusResponse> addFeeStatus(@RequestBody FeeStatusRequest request) {
        return ResponseEntity.ok(feeStatusService.addFeeStatus(request));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<FeeStatusResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(feeStatusService.getFeeStatusByStudent(studentId));
    }
}