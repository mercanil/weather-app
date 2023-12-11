package com.mercan.weather.service;

import com.mercan.weather.entity.Reading;
import com.mercan.weather.entity.Sensor;
import com.mercan.weather.model.AppConstants;
import com.mercan.weather.model.SensorQueryResponse;
import com.mercan.weather.model.SensorQueryResponseWrapper;
import com.mercan.weather.model.SensorReadingRequest;
import com.mercan.weather.repository.SensorReadingRepositoryImpl;
import com.mercan.weather.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorReadingService {

    private final SensorReadingRepositoryImpl sensorReadingRepositoryImpl;
    private final SensorRepository sensorRepository;

    public Reading createReading(SensorReadingRequest dto) {

        Reading reading = Reading.builder()
                .id(UUID.randomUUID())
                .sensorId(dto.getSensorId())
                .temperature(dto.getTemperature())
                .humidity(dto.getHumidity())
                .windSpeed(dto.getWindSpeed())
                .readingTime(new Timestamp(System.currentTimeMillis())).build();

        sensorReadingRepositoryImpl.save(reading);
        log.info(String.format("Weather created with id='%s'", reading.getId()));
        return reading;

    }

    public SensorQueryResponseWrapper queryData(Optional<Set<UUID>> sensorIdOpt, List<AppConstants.Metric> metrics, AppConstants.Statistic statistic, Optional<Date> startDate, Optional<Date> endDate) {


        SensorQueryResponseWrapper responseWrapper = new SensorQueryResponseWrapper();
        Set<UUID> sensorIds = sensorIdOpt.orElse(retrieveAllSensorIds());
        boolean onlyLatestData = startDate.isEmpty() && endDate.isEmpty();
        List<SensorQueryResponse> result;

        if (onlyLatestData) {
            result = sensorReadingRepositoryImpl.fetchLatestData(sensorIds);
        } else {
            result = sensorReadingRepositoryImpl.query(sensorIds, metrics, statistic, startDate, endDate);
        }
        responseWrapper.setStartDate(startDate.orElse(null));
        responseWrapper.setEndDate(endDate.orElse(null));
        responseWrapper.setResult(result);
        return responseWrapper;
    }


    private Set<UUID> retrieveAllSensorIds() {

        return sensorRepository.findAll()
                .stream()
                .map(Sensor::getId)
                .collect(Collectors.toSet());
    }
}