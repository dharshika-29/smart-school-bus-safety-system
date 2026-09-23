
package com.busfleet.smartbussafety.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busfleet.smartbussafety.dto.BusResponse;
import com.busfleet.smartbussafety.dto.ErrorResponse;
import com.busfleet.smartbussafety.dto.LocationUpdateRequest;
import com.busfleet.smartbussafety.exception.ApiException;
import com.busfleet.smartbussafety.service.BusService;

@RestController
@RequestMapping("/api/bus")
public class BusController {

    private final BusService busService;

    @Value("${app.device.key}")
    private String deviceKey;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    // GET /api/bus/my -> parent dashboard data (JWT required, enforced in SecurityConfig)
    @GetMapping("/my")
    public ResponseEntity<BusResponse> myBus(@AuthenticationPrincipal Long userId) {
        if (userId == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Please login first");
        }
        return ResponseEntity.ok(busService.getMyBus(userId));
    }

    // PUT /api/bus/{busNumber}/location -> called by the bus GPS device / driver app.
    // Public in SecurityConfig; protected instead by the x-device-key header checked here.
    @PutMapping("/{busNumber}/location")
    public ResponseEntity<?> updateLocation(
            @PathVariable String busNumber,
            @RequestHeader(value = "x-device-key", required = false) String providedKey,
            @RequestBody LocationUpdateRequest req) {

        if (!constantTimeEquals(providedKey, deviceKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid device key"));
        }

        return ResponseEntity.ok(busService.updateLocation(busNumber, req));
    }

    private boolean constantTimeEquals(String given, String expected) {
        if (given == null || expected == null) return false;
        byte[] a = given.getBytes(StandardCharsets.UTF_8);
        byte[] b = expected.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(a, b);
    }
}