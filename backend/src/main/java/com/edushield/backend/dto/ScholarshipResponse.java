package com.edushield.backend.dto;

import com.edushield.backend.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScholarshipResponse {
    private Long id;
    private String name;
    private Double minIncome;
    private Double maxIncome;
    private Category category;
    private Double minMarks;
    private String state;
    private LocalDate deadline;
    private String description;
}