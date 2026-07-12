package com.edushield.backend.controller;

import com.edushield.backend.dto.RiskScoreResponse;
import com.edushield.backend.service.RiskScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
public class RiskScoreController {

    @Autowired
    private RiskScoringService riskScoringService;

    @PostMapping("/compute/{studentId}")
    public ResponseEntity<RiskScoreResponse> computeRisk(@PathVariable Long studentId) {
        return ResponseEntity.ok(riskScoringService.computeRisk(studentId));
    }

    @GetMapping("/history/{studentId}")
    public ResponseEntity<List<RiskScoreResponse>> getRiskHistory(@PathVariable Long studentId) {
        return ResponseEntity.ok(riskScoringService.getRiskHistory(studentId));
    }
}