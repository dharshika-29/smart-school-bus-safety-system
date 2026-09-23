package com.busfleet.smartbussafety.service;

import com.busfleet.smartbussafety.dto.NotificationResponse;
import com.busfleet.smartbussafety.entity.User;
import com.busfleet.smartbussafety.exception.ApiException;
import com.busfleet.smartbussafety.repository.NotificationRepository;
import com.busfleet.smartbussafety.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<NotificationResponse> getMyNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));

        return notificationRepository
                .findByBusNumberOrderByCreatedAtDesc(user.getBusNumber(), PageRequest.of(0, 20))
                .stream()
                .map(n -> new NotificationResponse(n.getId(), n.getMessage(), n.getCreatedAt()))
                .toList();
    }
}
