package com.edushield.backend.controller;

import com.edushield.backend.dto.MarksRequest;
import com.edushield.backend.dto.MarksResponse;
import com.edushield.backend.service.MarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marks")
public class MarksController {

    @Autowired
    private MarksService marksService;

    @PostMapping
    public ResponseEntity<MarksResponse> addMarks(@RequestBody MarksRequest request) {
        return ResponseEntity.ok(marksService.addMarks(request));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<MarksResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(marksService.getMarksByStudent(studentId));
    }
}