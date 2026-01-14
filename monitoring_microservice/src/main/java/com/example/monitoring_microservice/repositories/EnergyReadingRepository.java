package com.example.monitoring_microservice.repositories;

import com.example.monitoring_microservice.entities.EnergyReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnergyReadingRepository extends JpaRepository<EnergyReading, UUID> {
}
