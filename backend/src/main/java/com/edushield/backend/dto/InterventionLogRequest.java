package com.edushield.backend.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InterventionLogRequest {
    private Long studentId;
    private Long mentorId;
    private String note;
    private LocalDate logDate;
}