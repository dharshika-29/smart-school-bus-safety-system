package com.busfleet.smartbussafety.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentCreateRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Class name is required")
    private String className;

    private String rollNumber;
    private Long parentId;
    private Long busId;
}