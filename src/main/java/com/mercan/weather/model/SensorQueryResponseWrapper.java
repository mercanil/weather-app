package com.mercan.weather.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SensorQueryResponseWrapper {
    List<SensorQueryResponse> result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date startDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date endDate;
}
