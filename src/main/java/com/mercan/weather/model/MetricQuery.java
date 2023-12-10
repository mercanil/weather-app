package com.mercan.weather.model;

import com.mercan.weather.model.AppConstants;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetricQuery {
    private AppConstants.Metric metricField;
    private String metric;
    private String metricAlias;
}
