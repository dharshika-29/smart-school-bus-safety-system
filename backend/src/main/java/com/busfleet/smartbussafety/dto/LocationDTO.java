package com.busfleet.smartbussafety.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LocationDTO {
    private Long id;
    private Long busId;
    private Double latitude;
    private Double longitude;
    private String address;
    private LocalDateTime timestamp;
}