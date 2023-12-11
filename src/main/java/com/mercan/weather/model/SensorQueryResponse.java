package com.mercan.weather.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SensorQueryResponse {
    private UUID sensorId;
    private List<MetricStatistic> statistics;
}
