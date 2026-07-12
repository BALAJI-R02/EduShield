package com.edushield.backend.service;

import com.edushield.backend.dto.FeeStatusRequest;
import com.edushield.backend.dto.FeeStatusResponse;
import com.edushield.backend.entity.FeeStatus;
import com.edushield.backend.entity.Student;
import com.edushield.backend.repository.FeeStatusRepository;
import com.edushield.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeeStatusService {

    @Autowired
    private FeeStatusRepository feeStatusRepository;

    @Autowired
    private StudentRepository studentRepository;

    public FeeStatusResponse addFeeStatus(FeeStatusRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        FeeStatus feeStatus = new FeeStatus();
        feeStatus.setStudent(student);
        feeStatus.setSemester(request.getSemester());
        feeStatus.setAmountDue(request.getAmountDue());
        feeStatus.setDueDate(request.getDueDate());
        feeStatus.setPaid(request.getPaid() != null ? request.getPaid() : false);

        FeeStatus saved = feeStatusRepository.save(feeStatus);
        return mapToResponse(saved);
    }

    public List<FeeStatusResponse> getFeeStatusByStudent(Long studentId) {
        return feeStatusRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private FeeStatusResponse mapToResponse(FeeStatus feeStatus) {
        return new FeeStatusResponse(
                feeStatus.getId(),
                feeStatus.getStudent().getId(),
                feeStatus.getSemester(),
                feeStatus.getAmountDue(),
                feeStatus.getDueDate(),
                feeStatus.getPaid()
        );
    }
}