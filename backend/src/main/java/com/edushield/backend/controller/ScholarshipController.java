package com.edushield.backend.controller;

import com.edushield.backend.dto.ScholarshipMatchResponse;
import com.edushield.backend.dto.ScholarshipRequest;
import com.edushield.backend.dto.ScholarshipResponse;
import com.edushield.backend.service.ScholarshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.edushield.backend.dto.ScholarshipMatchResponse;

import java.util.List;

@RestController
@RequestMapping("/api/scholarships")
public class ScholarshipController {

    @Autowired
    private ScholarshipService scholarshipService;

    @PostMapping
    public ResponseEntity<ScholarshipResponse> createScholarship(@RequestBody ScholarshipRequest request) {
        return ResponseEntity.ok(scholarshipService.createScholarship(request));
    }

    @GetMapping
    public ResponseEntity<List<ScholarshipResponse>> getAllScholarships() {
        return ResponseEntity.ok(scholarshipService.getAllScholarships());
    }
    @GetMapping("/match/{studentId}")
    public ResponseEntity<List<ScholarshipMatchResponse>> getMatches(@PathVariable Long studentId) {
        return ResponseEntity.ok(scholarshipService.findAndSaveMatches(studentId));
    }
}