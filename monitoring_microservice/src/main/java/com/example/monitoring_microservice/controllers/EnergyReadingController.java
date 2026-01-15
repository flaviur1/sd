package com.example.monitoring_microservice.controllers;

import com.example.monitoring_microservice.entities.Device;
import com.example.monitoring_microservice.repositories.DeviceRepository;
import com.example.monitoring_microservice.services.EnergyReadingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/monitor")
public class EnergyReadingController {

    private final EnergyReadingService energyReadingService;
    private final DeviceRepository deviceRepository;

    public EnergyReadingController(EnergyReadingService energyReadingService, DeviceRepository deviceRepository) {
        this.energyReadingService = energyReadingService;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/{id}/{date}")
    public ResponseEntity<?> getMonitorDataForDevice(@PathVariable UUID id, @PathVariable LocalDate date) {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication == null || !(authentication.getPrincipal() instanceof UUID)) {
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user not authenticated");
        // }
        // UUID userId = (UUID) authentication.getPrincipal();
        // Optional<Device> deviceOpt = deviceRepository.findById(id);
        // if (deviceOpt.isEmpty()) {
        //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body("device not found");
        // }
        // Device device = deviceOpt.get();
        // if (!device.getUserId().equals(userId)) {
        //     return ResponseEntity.status(HttpStatus.FORBIDDEN).body("device does not belong to user");
        // }
        

        return ResponseEntity.ok(energyReadingService.getMonitorDataForDevice(id, date));
    }
}
