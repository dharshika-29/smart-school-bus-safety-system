package com.busfleet.smartbussafety.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busfleet.smartbussafety.dto.BusResponse;
import com.busfleet.smartbussafety.dto.LocationUpdateRequest;
import com.busfleet.smartbussafety.entity.Bus;
import com.busfleet.smartbussafety.entity.Notification;
import com.busfleet.smartbussafety.entity.User;
import com.busfleet.smartbussafety.exception.ApiException;
import com.busfleet.smartbussafety.repository.BusRepository;
import com.busfleet.smartbussafety.repository.NotificationRepository;
import com.busfleet.smartbussafety.repository.UserRepository;

@Service
public class BusService {

    private final BusRepository busRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public BusService(BusRepository busRepository, UserRepository userRepository,
                       NotificationRepository notificationRepository) {
        this.busRepository = busRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public BusResponse getMyBus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));

        Bus bus = busRepository.findByBusNumber(user.getBusNumber())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Bus not found"));

        return toResponse(bus);
    }

    public List<Map<String, Object>> getAllBusesSimple() {
        return busRepository.findAll().stream()
                .map(bus -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", bus.getId());
                    m.put("busNumber", bus.getBusNumber());
                    return m;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public BusResponse updateLocation(String busNumber, LocationUpdateRequest req) {
        Bus bus = busRepository.findByBusNumber(busNumber)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Bus not found"));

        Bus.BusStatus oldStatus = bus.getStatus();
        String oldLocation = bus.getLocationName();

        if (req.getStatus() != null) {
            bus.setStatus(parseStatus(req.getStatus()));
        }
        if (req.getLocationName() != null) {
            bus.setLocationName(req.getLocationName());
        }
        if (req.getLat() != null) {
            bus.setLat(req.getLat());
        }
        if (req.getLng() != null) {
            bus.setLng(req.getLng());
        }

        bus = busRepository.save(bus);

        if (bus.getStatus() != oldStatus) {
            notify(bus.getBusNumber(), "Bus " + bus.getBusNumber() + " status: "
                    + bus.getStatus() + " (" + bus.getLocationName() + ")");
        } else if (!bus.getLocationName().equals(oldLocation)) {
            notify(bus.getBusNumber(), "Bus " + bus.getBusNumber() + " is now at " + bus.getLocationName());
        }

        return toResponse(bus);
    }

    private Bus.BusStatus parseStatus(String raw) {
        try {
            return Bus.BusStatus.valueOf(raw.trim().toUpperCase().replace(' ', '_'));
        } catch (IllegalArgumentException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Status must be one of: " + Arrays.toString(Bus.BusStatus.values()));
        }
    }

    private void notify(String busNumber, String message) {
        notificationRepository.save(Notification.builder()
                .busNumber(busNumber)
                .message(message)
                .build());
    }

    private BusResponse toResponse(Bus bus) {
        return new BusResponse(bus.getBusNumber(), bus.getStatus().name(),
                bus.getLocationName(), bus.getLat(), bus.getLng(), bus.getUpdatedAt());
    }
}