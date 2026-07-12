package com.edushield.backend.service;
import com.edushield.backend.entity.Category;
import com.edushield.backend.dto.ScholarshipMatchResponse;

import com.edushield.backend.dto.ScholarshipRequest;
import com.edushield.backend.dto.ScholarshipResponse;
import com.edushield.backend.entity.ApplicationStatus;
import com.edushield.backend.entity.Marks;
import com.edushield.backend.entity.Scholarship;
import com.edushield.backend.entity.ScholarshipMatch;
import com.edushield.backend.entity.Student;
import com.edushield.backend.repository.MarksRepository;
import com.edushield.backend.repository.ScholarshipMatchRepository;
import com.edushield.backend.repository.ScholarshipRepository;
import com.edushield.backend.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScholarshipService {

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScholarshipMatchRepository scholarshipMatchRepository;

    @Autowired
    private MarksRepository marksRepository;

    public ScholarshipResponse createScholarship(ScholarshipRequest request) {
        Scholarship scholarship = new Scholarship();
        mapRequestToEntity(request, scholarship);
        Scholarship saved = scholarshipRepository.save(scholarship);
        return mapToResponse(saved);
    }

    public List<ScholarshipResponse> getAllScholarships() {
        return scholarshipRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ScholarshipMatchResponse> findAndSaveMatches(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        double marksAvg = calculateAverageMarks(marksRepository.findByStudentIdOrderByAssessmentDateAsc(studentId));

        List<Scholarship> matches = scholarshipRepository.findMatchingScholarships(
        student.getAnnualIncome(),
        Category.valueOf(student.getCategory()),
        marksAvg,
        student.getState(),
        LocalDate.now()
        );

        return matches.stream().map(scholarship -> {
            boolean alreadyMatched = scholarshipMatchRepository.findByStudentId(studentId)
                    .stream()
                    .anyMatch(m -> m.getScholarship().getId().equals(scholarship.getId()));

            ScholarshipMatch match;
            if (!alreadyMatched) {
                match = new ScholarshipMatch();
                match.setStudent(student);
                match.setScholarship(scholarship);
                match.setMatchedAt(LocalDateTime.now());
                match.setAppliedStatus(ApplicationStatus.NOT_APPLIED);
                scholarshipMatchRepository.save(match);
            } else {
                match = scholarshipMatchRepository.findByStudentId(studentId).stream()
                        .filter(m -> m.getScholarship().getId().equals(scholarship.getId()))
                        .findFirst().get();
            }

            return new ScholarshipMatchResponse(
                    scholarship.getId(),
                    scholarship.getName(),
                    scholarship.getDescription(),
                    scholarship.getDeadline(),
                    match.getMatchedAt(),
                    match.getAppliedStatus()
            );
        }).collect(Collectors.toList());
    }

    private void mapRequestToEntity(ScholarshipRequest request, Scholarship scholarship) {
        scholarship.setName(request.getName());
        scholarship.setMinIncome(request.getMinIncome());
        scholarship.setMaxIncome(request.getMaxIncome());
        scholarship.setCategory(request.getCategory());
        scholarship.setMinMarks(request.getMinMarks());
        scholarship.setState(request.getState());
        scholarship.setDeadline(request.getDeadline());
        scholarship.setDescription(request.getDescription());
    }

    private ScholarshipResponse mapToResponse(Scholarship scholarship) {
        return new ScholarshipResponse(
                scholarship.getId(),
                scholarship.getName(),
                scholarship.getMinIncome(),
                scholarship.getMaxIncome(),
                scholarship.getCategory(),
                scholarship.getMinMarks(),
                scholarship.getState(),
                scholarship.getDeadline(),
                scholarship.getDescription()
        );
    }

    private double calculateAverageMarks(List<Marks> marksList) {
        if (marksList.isEmpty()) return 100.0;
        return marksList.stream()
                .mapToDouble(m -> (m.getScore() / m.getMaxScore()) * 100.0)
                .average()
                .orElse(100.0);
    }
}