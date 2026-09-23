package com.busfleet.smartbussafety.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busfleet.smartbussafety.dto.LocationDTO;
import com.busfleet.smartbussafety.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public LocationDTO updateLocation(@RequestBody Map<String, Object> body) {
        Long busId = Long.valueOf(body.get("busId").toString());
        Double latitude = Double.valueOf(body.get("latitude").toString());
        Double longitude = Double.valueOf(body.get("longitude").toString());
        String address = body.get("address") != null ? body.get("address").toString() : null;
        return locationService.updateLocation(busId, latitude, longitude, address);
    }

    @GetMapping("/bus/{busId}/latest")
    public LocationDTO getLatestLocation(@PathVariable Long busId) {
        return locationService.getLatestLocation(busId);
    }
}