package com.edushield.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edushield.backend.dto.MentorRequest;
import com.edushield.backend.dto.MentorResponse;
import com.edushield.backend.entity.Mentor;
import com.edushield.backend.repository.MentorRepository;

@Service
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    public MentorResponse createMentor(MentorRequest request) {
        Mentor mentor = new Mentor();
        mentor.setName(request.getName());
        mentor.setDepartment(request.getDepartment());
        Mentor saved = mentorRepository.save(mentor);
        return mapToResponse(saved);
    }

    public List<MentorResponse> getAllMentors() {
        return mentorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MentorResponse getMentorById(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor not found with id: " + id));
        return mapToResponse(mentor);
    }

    private MentorResponse mapToResponse(Mentor mentor) {
        return new MentorResponse(mentor.getId(), mentor.getName(), mentor.getDepartment());
    }
}