package com.example.device_microservice.dtos.builder;

import com.example.device_microservice.dtos.DeviceDTO;
import com.example.device_microservice.dtos.DeviceDetailsDTO;
import com.example.device_microservice.entities.Device;

public class DeviceBuilder {
    private DeviceBuilder() {

    }

    public static DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getManufacturer(), device.getMaxConsVal());
    }

    public static DeviceDetailsDTO toDeviceDetailsDTO(Device device) {
        return new DeviceDetailsDTO(device.getId(), device.getManufacturer(), device.getModel(), device.getMaxConsVal());
    }

    public static Device toEntity(DeviceDetailsDTO deviceDetailsDTO) {
        return new Device(deviceDetailsDTO.getManufacturer(), deviceDetailsDTO.getModel(), deviceDetailsDTO.getMaxConsVal());    }
}
