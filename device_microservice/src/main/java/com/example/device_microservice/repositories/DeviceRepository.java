package com.example.device_microservice.repositories;

import com.example.device_microservice.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    List<Device> getAllByUserId(UUID userId);
}
