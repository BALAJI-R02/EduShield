package com.edushield.backend.dto;

import com.edushield.backend.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;

    // Used when role = STUDENT or MENTOR
    private String name;
    private String department;

    // Used only when role = STUDENT
    private String rollNo;
    private Integer year;
    private String category;
    private Double annualIncome;
    private String state;
}