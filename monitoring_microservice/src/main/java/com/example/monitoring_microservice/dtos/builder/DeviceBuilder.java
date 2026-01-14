package com.example.monitoring_microservice.dtos.builder;

import com.example.monitoring_microservice.dtos.DeviceDTO;
import com.example.monitoring_microservice.dtos.DeviceDetailsDTO;
import com.example.monitoring_microservice.entities.Device;

public class DeviceBuilder {
    private DeviceBuilder() {

    }

    public static DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getUserId());
    }

    public static DeviceDetailsDTO toDeviceDetailsDTO(Device device) {
        return new DeviceDetailsDTO(device.getId(), device.getMaxConsVal(), device.getUserId());
    }

    public static Device toEntity(DeviceDetailsDTO deviceDetailsDTO) {
        return new Device(deviceDetailsDTO.getMaxConsVal(), deviceDetailsDTO.getUserId());
    }
}
