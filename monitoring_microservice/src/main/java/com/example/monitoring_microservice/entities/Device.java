package com.example.monitoring_microservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Device implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(name = "max_consumption", nullable = false)
    private int maxConsVal;

    @Column(name = "user_id")
    private UUID userId;

    public Device() {
    }

    public Device(int maxConsVal, UUID userID) {
        this.maxConsVal = maxConsVal;
        this.userId = userID;
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
}
