package com.mercan.weather.controller;

import com.mercan.weather.entity.Reading;
import com.mercan.weather.model.SensorQueryResponseWrapper;
import com.mercan.weather.model.SensorReadingRequest;
import com.mercan.weather.service.SensorReadingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
public class SensorReadingControllerTest {

    @Mock
    private SensorReadingService sensorReadingService;

    @InjectMocks
    private SensorReadingController sensorReadingController;

    private SensorQueryResponseWrapper queryResponse;
    private Reading reading;
    private UUID sensorId;
    private List<String> metrics;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    void setUp() {
        queryResponse = new SensorQueryResponseWrapper();
        reading = new Reading();
        sensorId = UUID.randomUUID();
        metrics = Arrays.asList("temperature", "humidity");
        startDate = new Date(); // set appropriate date
        endDate = new Date();   // set appropriate date

        // Initialize other necessary properties
    }

    @Test
    void testQuery() {
        Set<UUID> sensorIds = new HashSet<>(Arrays.asList(sensorId));
        when(sensorReadingService.queryData(sensorIds, metrics, "AVG", Optional.of(startDate), Optional.of(endDate)))
                .thenReturn(queryResponse);

        ResponseEntity<SensorQueryResponseWrapper> response = sensorReadingController.query(
                sensorIds, metrics, "AVG", startDate, endDate);

        assertEquals(OK, response.getStatusCode());
        assertEquals(queryResponse, response.getBody());
        verify(sensorReadingService).queryData(sensorIds, metrics, "AVG", Optional.of(startDate), Optional.of(endDate));
    }

    @Test
    void testCreateReading() {
        SensorReadingRequest dto = new SensorReadingRequest();
        // Set necessary properties on dto

        when(sensorReadingService.createReading(dto)).thenReturn(reading);

        ResponseEntity<Reading> response = sensorReadingController.createReading(dto);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(reading, response.getBody());
        verify(sensorReadingService).createReading(dto);
    }

}