package com.edushield.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String name;
    private String rollNo;
    private String department;
    private Integer year;
    private String category;
    private Double annualIncome;
    private String state;
    private Long mentorId;
    private String mentorName;
}