package com.example.monitoring_microservice.services;

import com.example.monitoring_microservice.dtos.HourlyConsumptionDTO;
import com.example.monitoring_microservice.entities.EnergyReading;
import com.example.monitoring_microservice.repositories.EnergyReadingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class EnergyReadingService {
    private final EnergyReadingRepository energyReadingRepository;

    public EnergyReadingService(EnergyReadingRepository energyReadingRepository) {
        this.energyReadingRepository = energyReadingRepository;
    }

    public List<HourlyConsumptionDTO> getMonitorDataForDevice(UUID deviceId, LocalDate date) {
        List<EnergyReading> readings = energyReadingRepository.findByDeviceIdAndDate(deviceId, date);
        Map<Integer, List<Double>> hourlyReadings = new HashMap<>();
        for (EnergyReading reading : readings) {
            Integer hour = reading.getHour();
            Double consumption = reading.getConsumptionValue();
            if (!hourlyReadings.containsKey(hour)) {
                hourlyReadings.put(hour, new ArrayList<>());
            }

            hourlyReadings.get(hour).add(consumption);
        }
        List<HourlyConsumptionDTO> hourlyList = new ArrayList<>();
        for (Map.Entry<Integer, List<Double>> entry : hourlyReadings.entrySet()) {
            Integer hour = entry.getKey();
            List<Double> consumptions = entry.getValue();
            double sum = 0;
            for (Double consumption : consumptions) {
                sum += consumption;
            }
            double average = sum / consumptions.size();
            hourlyList.add(new HourlyConsumptionDTO(hour, average));
        }
        hourlyList.sort(Comparator.comparing(HourlyConsumptionDTO::getHour));
        return hourlyList;
    }
}
