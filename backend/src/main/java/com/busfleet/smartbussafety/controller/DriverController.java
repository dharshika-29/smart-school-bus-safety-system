package com.busfleet.smartbussafety.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busfleet.smartbussafety.dto.DriverDTO;
import com.busfleet.smartbussafety.service.DriverService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public List<DriverDTO> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @PostMapping
    public DriverDTO createDriver(@RequestBody DriverDTO dto) {
        return driverService.createDriver(dto);
    }

    @PutMapping("/{id}")
    public DriverDTO updateDriver(@PathVariable Long id, @RequestBody DriverDTO dto) {
        return driverService.updateDriver(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
    }
}