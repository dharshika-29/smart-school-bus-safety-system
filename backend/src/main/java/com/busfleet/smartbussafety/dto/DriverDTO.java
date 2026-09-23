package com.busfleet.smartbussafety.dto;

import lombok.Data;

@Data
public class DriverDTO {
    private Long id;
    private String name;
    private String phone;
    private String licenseNumber;
    private String email;
}