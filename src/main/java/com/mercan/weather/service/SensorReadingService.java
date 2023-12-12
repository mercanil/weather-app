package com.mercan.weather.service;

import com.mercan.weather.entity.Reading;
import com.mercan.weather.entity.Sensor;
import com.mercan.weather.model.AppConstants;
import com.mercan.weather.model.SensorQueryResponse;
import com.mercan.weather.model.SensorQueryResponseWrapper;
import com.mercan.weather.model.SensorReadingRequest;
import com.mercan.weather.repository.SensorReadingRepository;
import com.mercan.weather.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;

    public Reading createReading(SensorReadingRequest dto) {
        Reading reading = Reading.builder()
                .id(UUID.randomUUID())
                .sensorId(dto.getSensorId())
                .temperature(dto.getTemperature())
                .humidity(dto.getHumidity())
                .windSpeed(dto.getWindSpeed())
                .time(new Timestamp(System.currentTimeMillis()))
                .build();

        sensorReadingRepository.save(reading);
        log.info("Weather created with id='{}'", reading.getId());
        return reading;
    }

    public SensorQueryResponseWrapper queryData(Optional<Set<UUID>> sensorIdOpt, List<String> metrics,
                                                String statistic, Optional<LocalDate> startDate,
                                                Optional<LocalDate> endDate) {


        List<AppConstants.Metric> metricAsEnum = metrics.stream().map(e -> AppConstants.Metric.valueOf(e)).collect(Collectors.toList());
        AppConstants.Statistic statisticEnum = AppConstants.Statistic.valueOf(statistic);
        Set<UUID> sensorIds = sensorIdOpt.orElseGet(this::retrieveAllSensorIds);
        List<SensorQueryResponse> result = isFetchingLatestData(startDate, endDate) ?
                sensorReadingRepository.fetchLatestData(sensorIds) :
                sensorReadingRepository.query(sensorIds, metricAsEnum, statisticEnum, startDate, endDate);

        return new SensorQueryResponseWrapper(result, startDate.orElse(null), endDate.orElse(null));
    }

    private Set<UUID> retrieveAllSensorIds() {
        return sensorRepository.findAll()
                .stream()
                .map(Sensor::getId)
                .collect(Collectors.toSet());
    }

    private boolean isFetchingLatestData(Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
        return startDate.isEmpty() && endDate.isEmpty();
    }
}