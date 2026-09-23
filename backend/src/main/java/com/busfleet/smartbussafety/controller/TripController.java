package com.busfleet.smartbussafety.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busfleet.smartbussafety.dto.TripDTO;
import com.busfleet.smartbussafety.entity.Trip;
import com.busfleet.smartbussafety.service.TripService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping
    public List<TripDTO> getAllTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("/bus/{busId}")
    public List<TripDTO> getTripsByBus(@PathVariable Long busId) {
        return tripService.getTripsByBus(busId);
    }

    @PostMapping("/start")
    public TripDTO startTrip(@RequestBody Map<String, Long> body) {
        return tripService.startTrip(body.get("busId"), body.get("driverId"), body.get("routeId"));
    }

    @PutMapping("/{id}/end")
    public TripDTO endTrip(@PathVariable Long id) {
        return tripService.endTrip(id);
    }

    @PutMapping("/{id}/status")
    public TripDTO updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return tripService.updateStatus(id, Trip.TripStatus.valueOf(body.get("status")));
    }
}