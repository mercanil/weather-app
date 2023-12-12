package com.mercan.weather.service;

import com.mercan.weather.model.AppConstants;
import com.mercan.weather.model.SensorQueryResponse;
import com.mercan.weather.model.SensorQueryResponseWrapper;
import com.mercan.weather.repository.SensorReadingRepository;
import com.mercan.weather.repository.SensorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensorReadingServiceTest {

    @Mock
    private SensorReadingRepository sensorReadingRepositoryImpl;

    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private SensorReadingService sensorReadingService;


    @Test
    void queryData_latestData() {
        when(sensorRepository.findAll()).thenReturn(Collections.emptyList());
        when(sensorReadingRepositoryImpl.fetchLatestData(any())).thenReturn(Collections.singletonList(new SensorQueryResponse()));

        SensorQueryResponseWrapper result = sensorReadingService.queryData(Optional.empty(), Arrays.asList(AppConstants.Metric.temperature.toString()), AppConstants.Statistic.AVERAGE.toString(), Optional.empty(), Optional.empty());
        assertNotNull(result);
        assertFalse(result.getResult().isEmpty());
    }

    private Set<UUID> retrieveAllSensorIds() {
        // Mock implementation
        return new HashSet<>();
    }
}