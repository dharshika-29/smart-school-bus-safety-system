package com.busfleet.smartbussafety.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BusResponse {
    private String busNumber;
    private String status;
    private String locationName;
    private Double lat;
    private Double lng;
    private LocalDateTime updatedAt;
}
