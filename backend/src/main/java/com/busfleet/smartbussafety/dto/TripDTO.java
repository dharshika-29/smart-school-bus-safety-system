package com.busfleet.smartbussafety.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TripDTO {
    private Long id;
    private Long busId;
    private String busNumber;
    private Long driverId;
    private String driverName;
    private Long routeId;
    private String routeName;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}