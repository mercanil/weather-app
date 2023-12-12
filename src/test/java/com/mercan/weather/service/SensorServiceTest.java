package com.mercan.weather.service;

import com.mercan.weather.entity.Sensor;
import com.mercan.weather.exception.SensorNotFound;
import com.mercan.weather.repository.SensorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private SensorService sensorService;

    @Test
    void createSensor_success() {
        Sensor sensor = new Sensor();
        when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);

        Sensor result = sensorService.createSensor(sensor);
        assertNotNull(result);
        verify(sensorRepository).save(sensor);
    }

    @Test
    void getSensor_found() {
        UUID id = UUID.randomUUID();
        Sensor sensor = new Sensor();
        when(sensorRepository.findById(id)).thenReturn(Optional.of(sensor));

        Optional<Sensor> result = sensorService.getSensor(id);
        assertTrue(result.isPresent());
        assertEquals(sensor, result.get());
    }

    @Test
    void getSensor_notFound() {
        UUID id = UUID.randomUUID();
        when(sensorRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Sensor> result = sensorService.getSensor(id);
        assertFalse(result.isPresent());
    }

    @Test
    void deleteSensor_success() {
        UUID id = UUID.randomUUID();
        doNothing().when(sensorRepository).deleteById(id);

        sensorService.deleteSensor(id);
        verify(sensorRepository).deleteById(id);
    }

    @Test
    void getAllSensors_success() {
        List<Sensor> sensors = Arrays.asList(new Sensor(), new Sensor()); // Initialize with properties
        when(sensorRepository.findAll()).thenReturn(sensors);

        List<Sensor> result = sensorService.getAllSensors();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(sensors.size(), result.size());
    }

    @Test
    void updateSensor_success() throws SensorNotFound {
        UUID sensorId = UUID.randomUUID();
        Sensor sensor = new Sensor();
        sensor.setId(sensorId);
        when(sensorRepository.findById(sensorId)).thenReturn(Optional.of(sensor));
        when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);

        Sensor result = sensorService.updateSensor(sensorId, sensor);
        assertNotNull(result);
        assertEquals(sensorId, result.getId());
    }

    @Test
    void updateSensor_sensorNotFound() {
        UUID sensorId = UUID.randomUUID();
        Sensor sensor = new Sensor();
        sensor.setId(sensorId);
        when(sensorRepository.findById(sensorId)).thenReturn(Optional.empty());

        assertThrows(SensorNotFound.class, () -> sensorService.updateSensor(sensorId, sensor));
    }

    @Test
    void updateSensor_invalidParameter() {
        UUID sensorId = UUID.randomUUID();
        Sensor sensor = new Sensor();
        sensor.setId(UUID.randomUUID()); // Different ID

        assertThrows(IllegalArgumentException.class, () -> sensorService.updateSensor(sensorId, sensor));
    }
}
