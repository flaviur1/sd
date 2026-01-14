package com.example.monitoring_microservice.repositories;

import com.example.monitoring_microservice.entities.EnergyReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EnergyReadingRepository extends JpaRepository<EnergyReading, UUID> {

    List<EnergyReading> findByDeviceIdAndDate(UUID deviceId, LocalDate date);
}
