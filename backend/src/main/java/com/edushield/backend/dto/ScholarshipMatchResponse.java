package com.edushield.backend.dto;

import com.edushield.backend.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScholarshipMatchResponse {
    private Long scholarshipId;
    private String scholarshipName;
    private String description;
    private LocalDate deadline;
    private LocalDateTime matchedAt;
    private ApplicationStatus appliedStatus;
}