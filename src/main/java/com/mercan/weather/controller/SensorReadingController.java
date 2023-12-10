package com.mercan.weather.controller;

import com.mercan.weather.entity.Reading;
import com.mercan.weather.model.SensorQueryResponseWrapper;
import com.mercan.weather.model.SensorReadingRequest;
import com.mercan.weather.service.SensorReadingService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/reading")
@RequiredArgsConstructor
public class SensorReadingController {
    private final SensorReadingService sensorReadingService;


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<SensorQueryResponseWrapper> query(
            @RequestParam Set<UUID> sensorIds,
            @RequestParam @NotEmpty List<String> metrics,
            @RequestParam(name = "statistic", defaultValue = "AVG", required = false) String statistic,
            @RequestParam(defaultValue = "2023-07-01", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(defaultValue = "2023-07-31", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        return status(HttpStatus.OK).body(sensorReadingService.queryData(sensorIds, metrics, statistic, Optional.of(startDate), Optional.of(endDate)));
    }


    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Reading> createReading(@RequestBody SensorReadingRequest dto) {
        return status(HttpStatus.CREATED).body(sensorReadingService.createReading(dto));
    }


}

