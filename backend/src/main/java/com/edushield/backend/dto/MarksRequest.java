package com.edushield.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MarksRequest {
    private Long studentId;
    private String subject;
    private String assessmentType;
    private Double score;
    private Double maxScore;
    private LocalDate assessmentDate;
}