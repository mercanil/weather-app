package com.mercan.weather.controller;

import com.mercan.weather.entity.Sensor;
import com.mercan.weather.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sensor")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService sensorService;


    @Operation(summary = "View a list of available sensors")
    @GetMapping
    public List<Sensor> getAllSensors() {
        return sensorService.getAllSensors();
    }

    @Operation(summary = "Get a sensor by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable(value = "id") UUID sensorId) {
        return sensorService.getSensor(sensorId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Operation(summary = "Add a sensor")
    @PostMapping
    public Sensor createSensor(@RequestBody Sensor sensor) {
        return sensorService.createSensor(sensor);
    }

    @Operation(summary = "Update a sensor")
    @PutMapping("/{id}")
    public Sensor updateSensor(@PathVariable(value = "id") UUID sensorId, @RequestBody Sensor sensor) {
        return sensorService.updateSensor(sensorId, sensor);

    }

    @Operation(summary = "Delete a sensor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable(value = "id") UUID sensorId) {
        sensorService.deleteSensor(sensorId);
        return ResponseEntity.ok().build();
    }
}