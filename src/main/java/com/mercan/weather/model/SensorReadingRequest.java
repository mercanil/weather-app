package com.mercan.weather.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SensorReadingRequest {
    @NotBlank UUID sensor;
    @NotNull @DecimalMin(value = "-100") @DecimalMax(value = "100") double temperature;
    @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") double humidity;
    @NotNull @DecimalMin(value = "0") @DecimalMax(value = "500") double windSpeed;
}