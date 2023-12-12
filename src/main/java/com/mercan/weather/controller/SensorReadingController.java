package com.mercan.weather.controller;

import com.mercan.weather.entity.Reading;
import com.mercan.weather.model.SensorQueryResponseWrapper;
import com.mercan.weather.model.SensorReadingRequest;
import com.mercan.weather.service.SensorReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/reading")
@RequiredArgsConstructor
@Validated
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;

    @Operation(summary = "Create Sensor Readings", description = "Create sensor readings based on given request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created sensor reading",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reading.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reading> createReading(@Valid @RequestBody SensorReadingRequest dto) {
        Reading createdReading = sensorReadingService.createReading(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReading);
    }

    @Operation(summary = "Query Sensor Readings", description = "Retrieve sensor readings based on given criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor readings",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SensorQueryResponseWrapper.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SensorQueryResponseWrapper> query(
            @Parameter(description = "Set of sensor IDs to filter the readings") @RequestParam(required = false) Optional<Set<UUID>> sensorIds,
            @Parameter(description = "List of metrics to include in the response, e.g., 'temperature', 'humidity'") @RequestParam @NotEmpty List<String> metrics,
            @Parameter(description = "Statistic type for aggregating the data, e.g., 'AVERAGE', 'SUM'") @RequestParam(name = "statistic", defaultValue = "AVG", required = false) String statistic,
            @Parameter(description = "Start date for filtering the readings") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @Parameter(description = "End date for filtering the readings") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate
    ) {
        SensorQueryResponseWrapper response = sensorReadingService.queryData(sensorIds, metrics, statistic, startDate, endDate);
        return ResponseEntity.ok(response);
    }
}