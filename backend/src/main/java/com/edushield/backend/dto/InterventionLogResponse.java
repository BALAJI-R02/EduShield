package com.edushield.backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterventionLogResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long mentorId;
    private String mentorName;
    private String note;
    private LocalDate logDate;
}