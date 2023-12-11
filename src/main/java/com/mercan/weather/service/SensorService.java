package com.mercan.weather.service;

import com.mercan.weather.entity.Sensor;
import com.mercan.weather.exception.SensorNotFound;
import com.mercan.weather.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service

public class SensorService {
    private final SensorRepository sensorRepository;
    private static final Logger log = LoggerFactory.getLogger(SensorService.class);

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Sensor createSensor(Sensor sensor) {
        log.info("Creating or updating sensor with ID: {}", sensor.getId());
        return sensorRepository.save(sensor);
    }

    public Optional<Sensor> getSensor(UUID id) {
        log.info("Fetching sensor with ID: {}", id);
        return sensorRepository.findById(id);
    }

    public void deleteSensor(UUID id) {
        log.info("Deleting sensor with ID: {}", id);
        sensorRepository.deleteById(id);
    }

    public List<Sensor> getAllSensors() {
        log.info("Fetching all sensors");
        return sensorRepository.findAll();
    }

    public Sensor updateSensor(UUID sensorId, Sensor sensor) throws SensorNotFound {

        if (!sensorId.equals(sensor.getId())) {
            log.info("invalid parameter");
            throw new IllegalArgumentException("sensor id and body does not match");
        }

        Optional<Sensor> byId = sensorRepository.findById(sensorId);

        if (byId.isEmpty()) {
            log.error("unable to find with sensor id {}", sensorId);
            throw new SensorNotFound("unable to find with sensor id " + sensorId);
        }

        return sensorRepository.save(sensor);
    }
}