package com.edushield.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scholarships")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "min_income")
    private Double minIncome;

    @Column(name = "max_income")
    private Double maxIncome;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "min_marks")
    private Double minMarks;

    private String state;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(columnDefinition = "TEXT")
    private String description;
}