package com.example.device_microservice.dtos.rabbitMQ;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeviceCreatedEvent {
    private UUID deviceId;
    private int maxConsVal;
    private UUID userId;
    private LocalDateTime timestamp;

    public DeviceCreatedEvent() {
    }

    public DeviceCreatedEvent(UUID deviceId, int maxConsVal, UUID userId) {
        this.deviceId = deviceId;
        this.maxConsVal = maxConsVal;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public int getMaxConsVal() {
        return maxConsVal;
    }

    public void setMaxConsVal(int maxConsVal) {
        this.maxConsVal = maxConsVal;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
