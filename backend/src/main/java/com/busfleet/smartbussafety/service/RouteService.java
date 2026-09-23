package com.busfleet.smartbussafety.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.busfleet.smartbussafety.dto.RouteDTO;
import com.busfleet.smartbussafety.entity.Route;
import com.busfleet.smartbussafety.exception.ApiException;
import com.busfleet.smartbussafety.repository.RouteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public List<RouteDTO> getAllRoutes() {
        return routeRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public RouteDTO createRoute(RouteDTO dto) {
        Route route = new Route();
        route.setRouteName(dto.getRouteName());
        route.setStartPoint(dto.getStartPoint());
        route.setEndPoint(dto.getEndPoint());
        return toDTO(routeRepository.save(route));
    }

    public RouteDTO updateRoute(Long id, RouteDTO dto) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Route not found"));
        route.setRouteName(dto.getRouteName());
        route.setStartPoint(dto.getStartPoint());
        route.setEndPoint(dto.getEndPoint());
        return toDTO(routeRepository.save(route));
    }

    public void deleteRoute(Long id) {
        if (!routeRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Route not found");
        }
        routeRepository.deleteById(id);
    }

    private RouteDTO toDTO(Route route) {
        RouteDTO dto = new RouteDTO();
        dto.setId(route.getId());
        dto.setRouteName(route.getRouteName());
        dto.setStartPoint(route.getStartPoint());
        dto.setEndPoint(route.getEndPoint());
        return dto;
    }
}