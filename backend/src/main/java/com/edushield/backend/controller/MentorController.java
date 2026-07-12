package com.edushield.backend.controller;

import com.edushield.backend.dto.MentorRequest;
import com.edushield.backend.dto.MentorResponse;
import com.edushield.backend.repository.MentorRepository;
import com.edushield.backend.repository.UserRepository;
import com.edushield.backend.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.edushield.backend.entity.Mentor;
import com.edushield.backend.entity.User;

import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
public class MentorController {
    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MentorService mentorService;

    @PostMapping
    public ResponseEntity<MentorResponse> createMentor(@RequestBody MentorRequest request) {
        return ResponseEntity.ok(mentorService.createMentor(request));
    }

    @GetMapping
    public ResponseEntity<List<MentorResponse>> getAllMentors() {
        return ResponseEntity.ok(mentorService.getAllMentors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorResponse> getMentorById(@PathVariable Long id) {
        return ResponseEntity.ok(mentorService.getMentorById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<MentorResponse> getMyProfile(Authentication authentication) {
    String username = authentication.getName();
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    Mentor mentor = mentorRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("No mentor profile linked to this account"));
    return ResponseEntity.ok(mentorService.getMentorById(mentor.getId()));
    }
}