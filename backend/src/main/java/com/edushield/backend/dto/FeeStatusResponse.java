package com.edushield.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeStatusResponse {
    private Long id;
    private Long studentId;
    private Integer semester;
    private Double amountDue;
    private LocalDate dueDate;
    private Boolean paid;
}