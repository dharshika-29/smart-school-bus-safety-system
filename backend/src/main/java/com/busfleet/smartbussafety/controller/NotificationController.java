package com.busfleet.smartbussafety.controller;

import com.busfleet.smartbussafety.dto.NotificationResponse;
import com.busfleet.smartbussafety.exception.ApiException;
import com.busfleet.smartbussafety.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> myNotifications(@AuthenticationPrincipal Long userId) {
        if (userId == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Please login first");
        }
        return ResponseEntity.ok(notificationService.getMyNotifications(userId));
    }
}
