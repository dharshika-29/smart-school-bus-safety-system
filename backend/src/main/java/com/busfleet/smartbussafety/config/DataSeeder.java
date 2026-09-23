package com.busfleet.smartbussafety.config;

import com.busfleet.smartbussafety.entity.Bus;
import com.busfleet.smartbussafety.entity.Notification;
import com.busfleet.smartbussafety.repository.BusRepository;
import com.busfleet.smartbussafety.repository.NotificationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final BusRepository busRepository;
    private final NotificationRepository notificationRepository;

    public DataSeeder(BusRepository busRepository, NotificationRepository notificationRepository) {
        this.busRepository = busRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void run(String... args) {
        String busNumber = "TN-45-1234";
        if (busRepository.findByBusNumber(busNumber).isEmpty()) {
            Bus bus = busRepository.save(Bus.builder()
                    .busNumber(busNumber)
                    .status(Bus.BusStatus.RUNNING)
                    .locationName("School Road")
                    .lat(13.0827)
                    .lng(80.2707)
                    .build());

            notificationRepository.save(Notification.builder()
                    .busNumber(bus.getBusNumber())
                    .message("Bus " + bus.getBusNumber() + " started its trip")
                    .build());

            System.out.println("Demo bus created: " + busNumber);
        }
    }
}
