package com.example.websocket_and_chat_microservice.dtos.websocket;

import java.time.LocalDateTime;
import java.util.UUID;

public class EnergyReadingEvent {
    private UUID deviceId;
    private LocalDateTime timestamp;
    private Double consumptionValue;
    private int hour;

    public EnergyReadingEvent() {
    }

    public EnergyReadingEvent(UUID deviceId, LocalDateTime timestamp, Double consumptionValue, int hour) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.consumptionValue = consumptionValue;
        this.hour = hour;
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

    public Double getConsumptionValue() {
        return consumptionValue;
    }

    public void setConsumptionValue(Double consumptionValue) {
        this.consumptionValue = consumptionValue;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
