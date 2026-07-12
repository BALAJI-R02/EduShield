package com.edushield.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edushield.backend.dto.AuthResponse;
import com.edushield.backend.dto.LoginRequest;
import com.edushield.backend.dto.RegisterRequest;
import com.edushield.backend.entity.User;
import com.edushield.backend.repository.MentorRepository;
import com.edushield.backend.repository.StudentRepository;
import com.edushield.backend.repository.UserRepository;
import com.edushield.backend.security.JwtUtil;
import com.edushield.backend.entity.Student;
import com.edushield.backend.entity.Mentor;
import com.edushield.backend.entity.Role;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
        return ResponseEntity.badRequest().body("Username already taken");
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(request.getRole());
    User savedUser = userRepository.save(user);

    // NEW: create linked profile based on role
    if (request.getRole() == Role.STUDENT) {
        Student student = new Student();
        student.setUser(savedUser);
        student.setName(request.getName());
        student.setRollNo(request.getRollNo());
        student.setDepartment(request.getDepartment());
        student.setYear(request.getYear());
        student.setCategory(request.getCategory());
        student.setAnnualIncome(request.getAnnualIncome());
        student.setState(request.getState());
        studentRepository.save(student);
    } else if (request.getRole() == Role.MENTOR) {
        Mentor mentor = new Mentor();
        mentor.setUser(savedUser);
        mentor.setName(request.getName());
        mentor.setDepartment(request.getDepartment());
        mentorRepository.save(mentor);
    }
    // if ADMIN, no extra profile needed

    String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole().name());
    return ResponseEntity.ok(new AuthResponse(token, savedUser.getUsername(), savedUser.getRole().name()));
}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name()));
    }
}