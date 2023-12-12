package com.mercan.weather.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reading {

    @Id
    private UUID id;
    private UUID sensorId;
    private Double temperature;
    private Double humidity;
    private Double windSpeed;
    private LocalDate readingTime;
}
