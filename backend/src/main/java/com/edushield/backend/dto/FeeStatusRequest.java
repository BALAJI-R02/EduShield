package com.edushield.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FeeStatusRequest {
    private Long studentId;
    private Integer semester;
    private Double amountDue;
    private LocalDate dueDate;
    private Boolean paid;
}