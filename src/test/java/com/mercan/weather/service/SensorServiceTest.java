package com.mercan.weather.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mercan.weather.entity.Sensor;
import com.mercan.weather.repository.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class SensorServiceTests {
    @Mock
    private SensorRepository sensorRepository;
    @InjectMocks
    private SensorService sensorService;
    private Sensor sensor;
    private final UUID sensorId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        sensor = new Sensor();
        sensor.setId(sensorId);
    }

    @Test
    void testCreateOrUpdateSensor() {
        when(sensorRepository.save(sensor)).thenReturn(sensor);
        Sensor savedSensor = sensorService.createOrUpdateSensor(sensor);
        assertEquals(sensorId, savedSensor.getId());
        verify(sensorRepository).save(sensor);
    }

    @Test
    void testGetSensorFound() {
        when(sensorRepository.findById(sensorId)).thenReturn(Optional.of(sensor));
        Optional foundSensor = sensorService.getSensor(sensorId);
        assertTrue(foundSensor.isPresent());
        assertEquals(sensorId, foundSensor.get());
        verify(sensorRepository).findById(sensorId);
    }

    @Test
    void testGetSensorNotFound() {
        when(sensorRepository.findById(sensorId)).thenReturn(Optional.empty());
        Optional foundSensor = sensorService.getSensor(sensorId);
        assertFalse(foundSensor.isPresent());
        verify(sensorRepository).findById(sensorId);
    }

    @Test
    void testGetAllSensors() {
        when(sensorRepository.findAll()).thenReturn(Arrays.asList(sensor));
        List sensors = sensorService.getAllSensors();
        assertFalse(sensors.isEmpty());
        verify(sensorRepository).findAll();
    }

    @Test
    void testDeleteSensor() {
        doNothing().when(sensorRepository).deleteById(sensorId);
        sensorService.deleteSensor(sensorId);
        verify(sensorRepository).deleteById(sensorId);
    }
}