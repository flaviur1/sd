package com.example.monitoring_microservice.services;

import com.example.monitoring_microservice.repositories.EnergyReadingRepository;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService {
    private final EnergyReadingRepository energyReadingRepository;

    public MonitoringService(EnergyReadingRepository energyReadingRepository) {
        this.energyReadingRepository = energyReadingRepository;
    }

}
