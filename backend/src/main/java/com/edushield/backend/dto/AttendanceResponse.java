package com.edushield.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private Long id;
    private Long studentId;
    private Integer month;
    private Integer year;
    private Integer totalDays;
    private Integer presentDays;
    private Double percentage;
}