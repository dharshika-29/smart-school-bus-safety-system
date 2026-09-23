package com.busfleet.smartbussafety.dto;

import lombok.Data;

@Data
public class RouteDTO {
    private Long id;
    private String routeName;
    private String startPoint;
    private String endPoint;
}