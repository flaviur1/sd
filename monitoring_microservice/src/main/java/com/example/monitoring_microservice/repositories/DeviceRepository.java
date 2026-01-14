package com.example.monitoring_microservice.repositories;

import com.example.monitoring_microservice.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
