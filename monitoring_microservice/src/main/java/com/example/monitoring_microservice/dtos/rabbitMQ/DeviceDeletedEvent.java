package com.example.monitoring_microservice.dtos.rabbitMQ;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeviceDeletedEvent {
    private UUID deviceId;
    private LocalDateTime timestamp;

    public DeviceDeletedEvent() {
    }

    public DeviceDeletedEvent(UUID deviceId) {
        this.deviceId = deviceId;
        this.timestamp = LocalDateTime.now();
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
}
