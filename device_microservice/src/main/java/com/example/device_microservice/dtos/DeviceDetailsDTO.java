package com.example.device_microservice.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;
import java.util.UUID;

public class DeviceDetailsDTO {
    private UUID id;
    @NotBlank(message = "manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "model is required")
    private String model;

    @NotBlank(message = "maximum consumption value is required")
    private int maxConsVal;

    private UUID userId;

    public DeviceDetailsDTO() {
    }

    public DeviceDetailsDTO(String manufacturer, String model, int maxConsVal) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.maxConsVal = maxConsVal;
    }

    public DeviceDetailsDTO(UUID id, String manufacturer, String model, int maxConsVal) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.maxConsVal = maxConsVal;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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
        return maxConsVal == that.maxConsVal &&
                Objects.equals(manufacturer, that.manufacturer) &&
                Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxConsVal, manufacturer, model);
    }
}
