package com.mercan.weather.controller;

import com.mercan.weather.entity.Sensor;
import com.mercan.weather.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

class SensorControllerTest {
    @Mock
    private SensorService sensorService;
    @InjectMocks
    private SensorController sensorController;
    private Sensor sensor;
    private final UUID sensorId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        sensor = new Sensor();
        sensor.setId(sensorId);
    }

    @Test
    void testGetAllSensors() {
        when(sensorService.getAllSensors()).thenReturn(Arrays.asList(sensor));
        List sensors = sensorController.getAllSensors();
        assertFalse(sensors.isEmpty());
        verify(sensorService).getAllSensors();
    }

    @Test
    void testGetSensorByIdFound() {
        when(sensorService.getSensor(sensorId)).thenReturn(Optional.of(sensor));
        ResponseEntity response = sensorController.getSensorById(sensorId);
        assertEquals(OK, response.getStatusCode());
        verify(sensorService).getSensor(sensorId);
    }

    @Test
    void testGetSensorByIdNotFound() {
        when(sensorService.getSensor(sensorId)).thenReturn(Optional.empty());
        ResponseEntity response = sensorController.getSensorById(sensorId);
        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(sensorService).getSensor(sensorId);
    }

    @Test
    void testCreateOrUpdateSensor() {
        when(sensorService.createOrUpdateSensor(sensor)).thenReturn(sensor);
        Sensor result = sensorController.createOrUpdateSensor(sensor);
        assertEquals(sensorId, result.getId());
        verify(sensorService).createOrUpdateSensor(sensor);
    }

    @Test
    void testDeleteSensor() {
        ResponseEntity response = sensorController.deleteSensor(sensorId);
        assertEquals(OK, response.getStatusCode());
        verify(sensorService).deleteSensor(sensorId);
    }
}