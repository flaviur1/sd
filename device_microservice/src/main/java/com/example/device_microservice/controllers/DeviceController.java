package com.example.device_microservice.controllers;

import com.example.device_microservice.dtos.DeviceDTO;
import com.example.device_microservice.dtos.DeviceDetailsDTO;
import com.example.device_microservice.entities.Device;
import com.example.device_microservice.services.DeviceService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return ResponseEntity.ok(deviceService.getAllDevices());
        } else {
            String username = authentication.getName();
            UUID currentUserId = authentication.

            return ResponseEntity.ok(deviceService.getAllDevicesForUserId(currentUserId));
        }
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
    public ResponseEntity<List<DeviceDetailsDTO>> getAllDevicesForUserId(@PathVariable UUID id){
        return ResponseEntity.ok(deviceService.getAllDevicesForUserId(id));
    }
}
