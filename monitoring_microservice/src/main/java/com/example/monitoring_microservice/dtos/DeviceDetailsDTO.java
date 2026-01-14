package com.example.monitoring_microservice.dtos;

import java.util.Objects;
import java.util.UUID;

public class DeviceDetailsDTO {
    private UUID id;
    private int maxConsVal;
    private UUID userId;

    public DeviceDetailsDTO() {

    }

    public DeviceDetailsDTO(UUID id, int maxConsVal, UUID userId) {
        this.id = id;
        this.maxConsVal = maxConsVal;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDetailsDTO that = (DeviceDetailsDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
