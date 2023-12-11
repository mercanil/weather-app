package com.mercan.weather.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetricFilter {
    private AppConstants.Metric metricField;
    private String metric;
    private String metricAlias;
}
