package com.mercan.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.mercan.weather.model.AppConstants.Statistic;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SensorQueryRequest implements Serializable {

    private List<UUID> sensorIds;
    private List<AppConstants.Metric> metrics;
    private Statistic statistic;
    private Date startDate;
    private Date endDate;

}
