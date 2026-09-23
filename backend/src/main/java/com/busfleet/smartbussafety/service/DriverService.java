package com.busfleet.smartbussafety.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.busfleet.smartbussafety.dto.DriverDTO;
import com.busfleet.smartbussafety.entity.Driver;
import com.busfleet.smartbussafety.exception.ApiException;
import com.busfleet.smartbussafety.repository.DriverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public List<DriverDTO> getAllDrivers() {
        return driverRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public DriverDTO createDriver(DriverDTO dto) {
        Driver driver = new Driver();
        driver.setName(dto.getName());
        driver.setPhone(dto.getPhone());
        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setEmail(dto.getEmail());
        return toDTO(driverRepository.save(driver));
    }

    public DriverDTO updateDriver(Long id, DriverDTO dto) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Driver not found"));
        driver.setName(dto.getName());
        driver.setPhone(dto.getPhone());
        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setEmail(dto.getEmail());
        return toDTO(driverRepository.save(driver));
    }

    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Driver not found");
        }
        driverRepository.deleteById(id);
    }

    private DriverDTO toDTO(Driver driver) {
        DriverDTO dto = new DriverDTO();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setPhone(driver.getPhone());
        dto.setLicenseNumber(driver.getLicenseNumber());
        dto.setEmail(driver.getEmail());
        return dto;
    }
}