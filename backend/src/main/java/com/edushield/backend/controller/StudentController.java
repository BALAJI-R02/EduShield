package com.edushield.backend.controller;

import com.edushield.backend.dto.StudentRequest;
import com.edushield.backend.dto.StudentResponse;
import com.edushield.backend.repository.StudentRepository;
import com.edushield.backend.repository.UserRepository;
import com.edushield.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.edushield.backend.entity.Student;
import com.edushield.backend.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.createStudent(request));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-mentor/{mentorId}")
    public ResponseEntity<List<StudentResponse>> getStudentsByMentor(@PathVariable Long mentorId) {
        return ResponseEntity.ok(studentService.getStudentsByMentor(mentorId));
    }

    @GetMapping("/me")
    public ResponseEntity<StudentResponse> getMyProfile(Authentication authentication) {
    String username = authentication.getName();
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    Student student = studentRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("No student profile linked to this account"));
    return ResponseEntity.ok(studentService.getStudentById(student.getId()));
    }
}