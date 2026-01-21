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
        return ResponseEntity.ok(energyReadingService.getMonitorDataForDevice(id, date));
    }
}
