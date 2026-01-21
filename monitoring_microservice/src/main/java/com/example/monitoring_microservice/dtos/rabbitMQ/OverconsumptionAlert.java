package com.example.monitoring_microservice.dtos.rabbitMQ;

import java.time.LocalDateTime;
import java.util.UUID;

public class OverconsumptionAlert {
    private UUID deviceId;
    private LocalDateTime timestamp;
    private Double actualConsumption;
    private int threshold;

    public OverconsumptionAlert() {
    }

    public OverconsumptionAlert(UUID deviceId, LocalDateTime timestamp, Double actualConsumption, int threshold) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.actualConsumption = actualConsumption;
        this.threshold = threshold;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getActualConsumption() {
        return actualConsumption;
    }

    public void setActualConsumption(Double actualConsumption) {
        this.actualConsumption = actualConsumption;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
