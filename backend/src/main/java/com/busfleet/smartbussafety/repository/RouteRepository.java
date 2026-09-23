package com.busfleet.smartbussafety.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busfleet.smartbussafety.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
}