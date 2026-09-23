package com.busfleet.smartbussafety.dto;

import lombok.Data;

@Data
public class StudentDTO {
    private Long id;
    private String name;
    private String className;
    private String rollNumber;
    private Long parentId;
    private String parentName;
    private Long busId;
    private String busNumber;
}