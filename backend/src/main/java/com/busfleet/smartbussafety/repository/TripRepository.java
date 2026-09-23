package com.busfleet.smartbussafety.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busfleet.smartbussafety.entity.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByBusId(Long busId);
    Optional<Trip> findByBusIdAndStatus(Long busId, Trip.TripStatus status);
}