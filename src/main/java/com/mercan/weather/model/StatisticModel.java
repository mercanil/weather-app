package com.mercan.weather.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class StatisticModel {
    private String metric;
    private Map<String, String> values;
}
