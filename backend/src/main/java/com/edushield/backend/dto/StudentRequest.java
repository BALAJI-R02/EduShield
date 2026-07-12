package com.edushield.backend.dto;

import lombok.Data;

@Data
public class StudentRequest {
    private String name;
    private String rollNo;
    private String department;
    private Integer year;
    private String category;
    private Double annualIncome;
    private String state;
    private Long mentorId;
}