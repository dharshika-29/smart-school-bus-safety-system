package com.busfleet.smartbussafety.repository;

import com.busfleet.smartbussafety.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByBusNumberOrderByCreatedAtDesc(String busNumber, Pageable pageable);
}
