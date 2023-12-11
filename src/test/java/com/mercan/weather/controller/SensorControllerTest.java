package com.mercan.weather.controller;

import com.mercan.weather.entity.Sensor;
import com.mercan.weather.exception.SensorNotFound;
import com.mercan.weather.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
public class SensorControllerTest {

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private SensorController sensorController;

    private Sensor sensor;
    private UUID sensorId;

    @BeforeEach
    void setUp() {
        sensor = new Sensor();
        sensorId = UUID.randomUUID();
        sensor.setId(sensorId);
        sensor.setName("test-sensor");
    }

    @Test
    void createSensorTest() {
        when(sensorService.createSensor(any(Sensor.class))).thenReturn(sensor);

        Sensor result = sensorController.createSensor(sensor).getBody();

        assertEquals(sensorId, result.getId());
        verify(sensorService).createSensor(sensor);
    }

    @Test
    void updateSensorTest() throws SensorNotFound {
        when(sensorService.updateSensor(eq(sensorId), any(Sensor.class))).thenReturn(sensor);

        Sensor result = sensorController.updateSensor(sensorId, sensor).getBody();

        assertEquals(sensorId, result.getId());
        verify(sensorService).updateSensor(sensorId, sensor);
    }

    @Test
    void deleteSensorTest() {
        doNothing().when(sensorService).deleteSensor(sensorId);

        ResponseEntity<Void> response = sensorController.deleteSensor(sensorId);

        assertEquals(OK, response.getStatusCode());
        verify(sensorService).deleteSensor(sensorId);
    }

    @Test
    void getSensorByIdNotFoundTest() {
        when(sensorService.getSensor(sensorId)).thenReturn(Optional.empty());

        ResponseEntity<Sensor> response = sensorController.getSensorById(sensorId);

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(sensorService).getSensor(sensorId);
    }


}