package com.mercan.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class MetricStatistic {
    private String metric;
    private Map<String, String> values;
}
