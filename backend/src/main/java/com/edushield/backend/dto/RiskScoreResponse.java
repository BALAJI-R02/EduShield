package com.edushield.backend.dto;

import com.edushield.backend.entity.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskScoreResponse {
    private Long studentId;
    private String studentName;
    private Double attendancePct;
    private Double marksAvg;
    private Boolean feeOverdue;
    private Double riskScoreValue;
    private RiskLevel riskLevel;
    private LocalDateTime computedAt;
}