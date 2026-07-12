package com.edushield.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edushield.backend.dto.InterventionLogRequest;
import com.edushield.backend.dto.InterventionLogResponse;
import com.edushield.backend.entity.InterventionLog;
import com.edushield.backend.entity.Mentor;
import com.edushield.backend.entity.Student;
import com.edushield.backend.repository.InterventionLogRepository;
import com.edushield.backend.repository.MentorRepository;
import com.edushield.backend.repository.StudentRepository;

@Service
public class InterventionLogService {

    @Autowired
    private InterventionLogRepository interventionLogRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MentorRepository mentorRepository;

    public InterventionLogResponse addLog(InterventionLogRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor not found with id: " + request.getMentorId()));

        InterventionLog log = new InterventionLog();
        log.setStudent(student);
        log.setMentor(mentor);
        log.setNote(request.getNote());
        log.setLogDate(request.getLogDate());

        InterventionLog saved = interventionLogRepository.save(log);
        return mapToResponse(saved);
    }

    public List<InterventionLogResponse> getLogsByStudent(Long studentId) {
        return interventionLogRepository.findByStudentIdOrderByLogDateDesc(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InterventionLogResponse mapToResponse(InterventionLog log) {
        return new InterventionLogResponse(
                log.getId(),
                log.getStudent().getId(),
                log.getStudent().getName(),
                log.getMentor().getId(),
                log.getMentor().getName(),
                log.getNote(),
                log.getLogDate()
        );
    }
}