package com.mercan.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercan.weather.entity.Reading;
import com.mercan.weather.model.MetricStatistic;
import com.mercan.weather.model.SensorQueryResponse;
import com.mercan.weather.model.SensorQueryResponseWrapper;
import com.mercan.weather.model.SensorReadingRequest;
import com.mercan.weather.service.SensorReadingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SensorReadingController.class)
class SensorReadingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SensorReadingService sensorReadingService;

    @Test
    void createReading_success() throws Exception {
        SensorReadingRequest request = new SensorReadingRequest();
        request.setSensorId(UUID.randomUUID());
        request.setHumidity(1);
        request.setTemperature(2);
        request.setWindSpeed(3);
        Reading reading = new Reading();
        when(sensorReadingService.createReading(any(SensorReadingRequest.class))).thenReturn(reading);

        mockMvc.perform(post("/api/reading")
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createReading_expect401_when_sensorid_empty() throws Exception {
        SensorReadingRequest request = new SensorReadingRequest();
        request.setHumidity(1);
        request.setTemperature(2);
        request.setWindSpeed(3);
        Reading reading = new Reading();
        when(sensorReadingService.createReading(any(SensorReadingRequest.class))).thenReturn(reading);

        mockMvc.perform(post("/api/reading")
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReading_expect401_when_humidity_empty() throws Exception {
        SensorReadingRequest request = new SensorReadingRequest();
        request.setSensorId(UUID.randomUUID());
        request.setTemperature(2);
        request.setWindSpeed(3);
        Reading reading = new Reading();
        when(sensorReadingService.createReading(any(SensorReadingRequest.class))).thenReturn(reading);

        mockMvc.perform(get("/api/reading")
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReading_invalidRequestParameter() throws Exception {
        SensorReadingRequest invalidRequest = new SensorReadingRequest();

        mockMvc.perform(post("/api/reading")
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void queryReading_withAllValues_1() throws Exception {

        //given
        SensorQueryResponseWrapper responseWrapper = new SensorQueryResponseWrapper();
        Map<String, String> values = new HashMap<>();
        values.put("AVG", "1");
        MetricStatistic temperature = MetricStatistic.builder().metric("temperature").values(values).build();
        List<MetricStatistic> statistics = List.of(temperature);
        SensorQueryResponse response = SensorQueryResponse.builder().statistics(statistics).build();
        List<SensorQueryResponse> result = List.of(response);
        responseWrapper.setResult(result);

        when(sensorReadingService.queryData(any(), any(), any(), any(), any())).thenReturn(responseWrapper);

        mockMvc.perform(get("/api/reading")
                        //.param("sensorIds", "123e4567-e89b-12d3-a456-426655440000")
                        .param("metrics", "temperature")
                        .param("statistic", "AVG")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-01-31")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result.[0].statistics.[0].values.AVG", is("1")));
    }


    @Test
    void queryReading_withAllValues_2() throws Exception {

        //given
        SensorQueryResponseWrapper responseWrapper = new SensorQueryResponseWrapper();
        Map<String, String> values = new HashMap<>();
        values.put("AVG", "1");
        MetricStatistic temperature = MetricStatistic.builder().metric("temperature").values(values).build();
        List<MetricStatistic> statistics = List.of(temperature);
        SensorQueryResponse response = SensorQueryResponse.builder().statistics(statistics).build();
        List<SensorQueryResponse> result = List.of(response);
        responseWrapper.setResult(result);

        when(sensorReadingService.queryData(any(), any(), any(), any(), any())).thenReturn(responseWrapper);

        mockMvc.perform(get("/api/reading")
                        .param("sensorIds", "123e4567-e89b-12d3-a456-426655440000")
                        .param("metrics", "temperature")
                        .param("statistic", "AVG")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-01-31")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result.[0].statistics.[0].values.AVG", is("1")));
    }

    @Test
    void queryReading_withoutSensorId_temperature() throws Exception {
        SensorQueryResponseWrapper responseWrapper = new SensorQueryResponseWrapper();
        SensorQueryResponse response = SensorQueryResponse.builder().temperature(1d).build();
        List<SensorQueryResponse> result = List.of(response);
        responseWrapper.setResult(result);
        when(sensorReadingService.queryData(any(), any(), any(), any(), any())).thenReturn(responseWrapper);
        mockMvc.perform(get("/api/reading")
                        .param("metrics", "temperature")
                        .contentType(APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result.[0].temperature", is(1.0)));
        // Passes
    }

    @Test
    void queryReading_withoutSensorId_humidity() throws Exception {
        SensorQueryResponseWrapper responseWrapper = new SensorQueryResponseWrapper();
        SensorQueryResponse response = SensorQueryResponse.builder().humidity(1d).build();
        List<SensorQueryResponse> result = List.of(response);
        responseWrapper.setResult(result);
        when(sensorReadingService.queryData(any(), any(), any(), any(), any())).thenReturn(responseWrapper);
        mockMvc.perform(get("/api/reading")
                        .param("metrics", "humidity")
                        .contentType(APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("result.[0].humidity", is(1.0)));
        // Passes
    }

}

