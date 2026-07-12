package com.edushield.backend.dto;

import lombok.Data;

@Data
public class AttendanceRequest {
    private Long studentId;
    private Integer month;
    private Integer year;
    private Integer totalDays;
    private Integer presentDays;
}