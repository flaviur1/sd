package com.example.monitoring_microservice.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
public class EnergyReading {
    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(name = "device_id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID deviceId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Integer hour;

    @Column(name = "consumption_value", nullable = false)
    private Double consumptionValue;

    public EnergyReading() {
    }

    public EnergyReading(UUID deviceId, LocalDateTime timestamp, Double consumptionValue) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.consumptionValue = consumptionValue;
        this.hour = timestamp.getHour();
        this.date = Date.from(timestamp.toLocalDate().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
        this.hour = timestamp.getHour();
        this.date = Date.from(timestamp.toLocalDate().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Double getConsumptionValue() {
        return consumptionValue;
    }

    public void setConsumptionValue(Double consumptionValue) {
        this.consumptionValue = consumptionValue;
    }
}
