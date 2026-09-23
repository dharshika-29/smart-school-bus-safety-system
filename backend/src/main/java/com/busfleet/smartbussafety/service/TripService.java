package com.busfleet.smartbussafety.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.busfleet.smartbussafety.dto.TripDTO;
import com.busfleet.smartbussafety.entity.Bus;
import com.busfleet.smartbussafety.entity.Driver;
import com.busfleet.smartbussafety.entity.Route;
import com.busfleet.smartbussafety.entity.Trip;
import com.busfleet.smartbussafety.exception.ApiException;
import com.busfleet.smartbussafety.repository.BusRepository;
import com.busfleet.smartbussafety.repository.DriverRepository;
import com.busfleet.smartbussafety.repository.RouteRepository;
import com.busfleet.smartbussafety.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final BusRepository busRepository;
    private final DriverRepository driverRepository;
    private final RouteRepository routeRepository;

    public List<TripDTO> getAllTrips() {
        return tripRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TripDTO> getTripsByBus(Long busId) {
        return tripRepository.findByBusId(busId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Business rule: a bus cannot start a new trip if it already has one STARTED
    public TripDTO startTrip(Long busId, Long driverId, Long routeId) {
        boolean alreadyRunning = tripRepository.findByBusIdAndStatus(busId, Trip.TripStatus.STARTED).isPresent();
        if (alreadyRunning) {
            throw new ApiException(HttpStatus.CONFLICT, "This bus already has an active trip");
        }

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Bus not found"));
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Driver not found"));

        Trip trip = new Trip();
        trip.setBus(bus);
        trip.setDriver(driver);
        if (routeId != null) {
            Route route = routeRepository.findById(routeId)
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Route not found"));
            trip.setRoute(route);
        }
        trip.setStatus(Trip.TripStatus.STARTED);
        trip.setStartedAt(LocalDateTime.now());
        return toDTO(tripRepository.save(trip));
    }

    // Business rule: cannot end a trip that was never started
    public TripDTO endTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Trip not found"));

        if (trip.getStatus() != Trip.TripStatus.STARTED && trip.getStatus() != Trip.TripStatus.IN_TRANSIT) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Trip is not currently active, cannot end it");
        }

        trip.setStatus(Trip.TripStatus.COMPLETED);
        trip.setEndedAt(LocalDateTime.now());
        return toDTO(tripRepository.save(trip));
    }

    public TripDTO updateStatus(Long tripId, Trip.TripStatus status) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Trip not found"));
        trip.setStatus(status);
        return toDTO(tripRepository.save(trip));
    }

    private TripDTO toDTO(Trip trip) {
        TripDTO dto = new TripDTO();
        dto.setId(trip.getId());
        dto.setBusId(trip.getBus().getId());
        dto.setBusNumber(trip.getBus().getBusNumber());
        dto.setDriverId(trip.getDriver().getId());
        dto.setDriverName(trip.getDriver().getName());
        if (trip.getRoute() != null) {
            dto.setRouteId(trip.getRoute().getId());
            dto.setRouteName(trip.getRoute().getRouteName());
        }
        dto.setStatus(trip.getStatus().name());
        dto.setStartedAt(trip.getStartedAt());
        dto.setEndedAt(trip.getEndedAt());
        return dto;
    }
}