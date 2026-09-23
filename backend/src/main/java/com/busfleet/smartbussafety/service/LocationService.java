package com.busfleet.smartbussafety.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.busfleet.smartbussafety.dto.LocationDTO;
import com.busfleet.smartbussafety.entity.Bus;
import com.busfleet.smartbussafety.entity.Location;
import com.busfleet.smartbussafety.exception.ApiException;
import com.busfleet.smartbussafety.repository.BusRepository;
import com.busfleet.smartbussafety.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final BusRepository busRepository;

    // Driver sends a new location -> stored as a log entry AND updates the bus's current lat/lng
    public LocationDTO updateLocation(Long busId, Double latitude, Double longitude, String address) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Bus not found"));

        Location location = new Location();
        location.setBus(bus);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAddress(address);
        Location saved = locationRepository.save(location);

        bus.setLat(latitude);
        bus.setLng(longitude);
        busRepository.save(bus);

        return toDTO(saved);
    }

    public LocationDTO getLatestLocation(Long busId) {
        Location location = locationRepository.findFirstByBusIdOrderByTimestampDesc(busId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No location data for this bus yet"));
        return toDTO(location);
    }

    private LocationDTO toDTO(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setBusId(location.getBus().getId());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setAddress(location.getAddress());
        dto.setTimestamp(location.getTimestamp());
        return dto;
    }
}