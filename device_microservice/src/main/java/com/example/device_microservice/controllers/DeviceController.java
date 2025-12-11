package com.example.device_microservice.controllers;

import com.example.device_microservice.dtos.DeviceDTO;
import com.example.device_microservice.dtos.DeviceDetailsDTO;
import com.example.device_microservice.services.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDetailsDTO> getDeviceById(@PathVariable UUID id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @PostMapping
    public ResponseEntity<Void> addDevice(@RequestBody DeviceDetailsDTO device) {
        UUID id = deviceService.insert(device);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build(); // 201 + Location header
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDetailsDTO> updateDeviceById(@PathVariable UUID id, @RequestBody DeviceDetailsDTO device) {
        return ResponseEntity.ok(deviceService.updateDeviceById(id, device));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteByID(@PathVariable UUID id) {
        return ResponseEntity.ok(deviceService.deleteById(id));
    }

    @GetMapping("/getFor/{id}")
    public ResponseEntity<List<DeviceDetailsDTO>> getAllDevicesForUserId(@PathVariable UUID id) {
        return ResponseEntity.ok(deviceService.getAllDevicesForUserId(id));
    }

    @PutMapping("/assign/{id}")
    public ResponseEntity<DeviceDetailsDTO> assignDeviceToUser(@PathVariable UUID id, @RequestBody DeviceDetailsDTO device) {
        return ResponseEntity.ok(deviceService.updateDeviceById(id, device));
    }
}
