package com.example.monitoring_microservice.dtos.rabbitMQ;

import java.time.LocalDateTime;
import java.util.UUID;

public class SimulatorMessage {
    private UUID deviceId;
    private Double value;
    private LocalDateTime timestamp;

    public SimulatorMessage() {
    }

    public SimulatorMessage(UUID deviceId, Double value, LocalDateTime timestamp) {
        this.deviceId = deviceId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public Double getValue() {
        return value;
    }

    public void setConsumptionValue(Double value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
