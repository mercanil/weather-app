package com.mercan.weather.service;

import com.mercan.weather.model.MetricQuery;
import com.mercan.weather.entity.Reading;
import com.mercan.weather.model.*;
import com.mercan.weather.repository.SensorReadingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;

    public Reading createReading(SensorReadingRequest dto) {

        Reading reading = Reading.builder().id(UUID.randomUUID()).sensorId(dto.getSensor()).temperature(dto.getTemperature()).humidity(dto.getHumidity()).windSpeed(dto.getWindSpeed()).readingTime(new Timestamp(System.currentTimeMillis())).build();
        sensorReadingRepository.save(reading);
        log.info(String.format("Weather created with id='%s'", reading.getId()));
        return reading;

    }

    public SensorQueryResponseWrapper queryData(Set<UUID> sensorIds, List<String> metrics, String statistic, Optional<Date> startDate, Optional<Date> endDate) {


        SensorQueryResponseWrapper responseWrapper = new SensorQueryResponseWrapper();
        List<SensorQueryResponse> result = sensorReadingRepository.query(sensorIds, metrics, statistic, startDate, endDate);
        responseWrapper.setEndDate(endDate.orElse(null));
        responseWrapper.setStartDate(startDate.orElse(null));
        responseWrapper.setResult(result);
        return responseWrapper;
    }
}