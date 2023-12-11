package com.mercan.weather.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorQueryResponse {
    private UUID sensorId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MetricStatistic> statistics;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double temperature;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double humidity;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double windSpeed;

}
