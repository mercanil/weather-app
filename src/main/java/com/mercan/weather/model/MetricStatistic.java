package com.mercan.weather.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class MetricStatistic {
    private String metric;
    private Map<String, String> values;
}
