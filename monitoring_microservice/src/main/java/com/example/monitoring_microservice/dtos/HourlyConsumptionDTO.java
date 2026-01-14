package com.example.monitoring_microservice.dtos;

public class HourlyConsumptionDTO {
    private Integer hour;
    private Double averageConsumption;

    public HourlyConsumptionDTO() {
    }

    public HourlyConsumptionDTO(Integer hour, Double averageConsumption) {
        this.hour = hour;
        this.averageConsumption = averageConsumption;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Double getAverageConsumption() {
        return averageConsumption;
    }

    public void setAverageConsumption(Double averageConsumption) {
        this.averageConsumption = averageConsumption;
    }
}
