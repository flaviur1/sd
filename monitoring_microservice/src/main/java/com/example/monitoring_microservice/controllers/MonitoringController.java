package com.example.monitoring_microservice.controllers;

import com.example.monitoring_microservice.dtos.HourlyConsumptionDTO;
import com.example.monitoring_microservice.entities.Device;
import com.example.monitoring_microservice.repositories.DeviceRepository;
import com.example.monitoring_microservice.services.MonitoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/monitor")
public class MonitoringController {

    private final MonitoringService monitoringService;
    private final DeviceRepository deviceRepository;

    public MonitoringController(MonitoringService monitoringService, DeviceRepository deviceRepository) {
        this.monitoringService = monitoringService;
        this.deviceRepository = deviceRepository;
    }
}
