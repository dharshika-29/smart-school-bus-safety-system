package com.busfleet.smartbussafety.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationUpdateRequest {
    // Null values-a ignore pannanum, so wrapper types (String/Double) use pannirukkom
    private String status;        // NOT_STARTED, RUNNING, STOPPED, REACHED_SCHOOL
    private String locationName;
    private Double lat;
    private Double lng;
}
