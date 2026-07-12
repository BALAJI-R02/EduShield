package com.edushield.backend.service;

import com.edushield.backend.dto.MarksRequest;
import com.edushield.backend.dto.MarksResponse;
import com.edushield.backend.entity.Marks;
import com.edushield.backend.entity.Student;
import com.edushield.backend.repository.MarksRepository;
import com.edushield.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarksService {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private StudentRepository studentRepository;

    public MarksResponse addMarks(MarksRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        Marks marks = new Marks();
        marks.setStudent(student);
        marks.setSubject(request.getSubject());
        marks.setAssessmentType(request.getAssessmentType());
        marks.setScore(request.getScore());
        marks.setMaxScore(request.getMaxScore());
        marks.setAssessmentDate(request.getAssessmentDate());

        Marks saved = marksRepository.save(marks);
        return mapToResponse(saved);
    }

    public List<MarksResponse> getMarksByStudent(Long studentId) {
        return marksRepository.findByStudentIdOrderByAssessmentDateAsc(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private MarksResponse mapToResponse(Marks marks) {
        double percentage = marks.getMaxScore() > 0
                ? (marks.getScore() * 100.0) / marks.getMaxScore()
                : 0.0;

        return new MarksResponse(
                marks.getId(),
                marks.getStudent().getId(),
                marks.getSubject(),
                marks.getAssessmentType(),
                marks.getScore(),
                marks.getMaxScore(),
                Math.round(percentage * 100.0) / 100.0,
                marks.getAssessmentDate()
        );
    }
}