package com.mercan.weather.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SensorQueryResponseWrapper {
    List<SensorQueryResponse> result;
    private Date startDate;
    private Date endDate;
}
