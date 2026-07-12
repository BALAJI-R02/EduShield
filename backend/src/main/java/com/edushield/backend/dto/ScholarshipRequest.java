package com.edushield.backend.dto;

import com.edushield.backend.entity.Category;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ScholarshipRequest {
    private String name;
    private Double minIncome;
    private Double maxIncome;
    private Category category;
    private Double minMarks;
    private String state;
    private LocalDate deadline;
    private String description;
}