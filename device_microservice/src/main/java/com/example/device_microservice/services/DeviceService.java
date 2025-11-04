package com.example.device_microservice.services;

import com.example.device_microservice.dtos.DeviceDTO;
import com.example.device_microservice.dtos.DeviceDetailsDTO;
import com.example.device_microservice.dtos.builder.DeviceBuilder;
import com.example.device_microservice.entities.Device;
import com.example.device_microservice.handlers.exceptions.model.ResourceNotFoundException;
import com.example.device_microservice.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final WebClient webClient;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
        this.webClient = WebClient.create();
    }

    public List<DeviceDTO> getAllDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDetailsDTO getDeviceById(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in database", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDetailsDTO(deviceOptional.get());
    }

    private void checkIfUserExists(UUID id) {
        if (id != null) {
            try {
                this.webClient.get()
                        .uri("http://user_microservice:8080/api/users/" + id) // Call the User Microservice endpoint
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                            // If 404 is returned, this block executes
                            LOGGER.error("User with id {} was not found for device assignment.", id);
                            return Mono.error(new ResourceNotFoundException("User with id: " + id));
                        })
                        .bodyToMono(Void.class) // We only care if the call succeeded or failed
                        .block(); // BLOCKING CALL: Enforces the synchronous request-reply
            } catch (WebClientException e) {
                // Handle connection or other WebClient errors
                throw new RuntimeException("Could not connect to User Microservice for validation.", e);
            }
        }
    }

    public UUID insert(DeviceDetailsDTO deviceDetailsDTO) {
        Device device = DeviceBuilder.toEntity(deviceDetailsDTO);
        checkIfUserExists(deviceDetailsDTO.getUserId());
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in database", device.getId());
        return device.getId();
    }

    public DeviceDetailsDTO updateDeviceById(UUID id, DeviceDetailsDTO device) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found.", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        checkIfUserExists(device.getUserId());
        Device d = deviceOptional.get();
        d.setId(device.getId());
        d.setManufacturer(device.getManufacturer());
        d.setModel(device.getModel());
        d.setMaxConsVal(device.getMaxConsVal());
        d.setUserId(device.getUserId());
        deviceRepository.save(d);
        return DeviceBuilder.toDeviceDetailsDTO(d);
    }

    public String deleteById(UUID id) {
        deviceRepository.deleteById(id);
        return "Device with id " + id + " has been deleted succesfully";
    }
}
