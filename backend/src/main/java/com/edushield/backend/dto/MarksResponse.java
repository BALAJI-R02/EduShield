package com.edushield.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarksResponse {
    private Long id;
    private Long studentId;
    private String subject;
    private String assessmentType;
    private Double score;
    private Double maxScore;
    private Double percentage;
    private LocalDate assessmentDate;
}