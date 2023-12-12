package com.mercan.weather.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercan.weather.model.SensorReadingRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class SensorReadingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16.1")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }


    @Test
    void createReading_invalidRequestParameter() throws Exception {
        SensorReadingRequest invalidRequest = new SensorReadingRequest();
        mockMvc.perform(post("/api/reading")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void queryReading_withAllValues() throws Exception {
        mockMvc.perform(get("/api/reading")
                        .param("sensorIds", UUID.randomUUID().toString())
                        .param("metrics", "temperature")
                        .param("statistic", "AVERAGE")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-01-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }

    @Test
    void queryReading_withoutSensorId() throws Exception {
        mockMvc.perform(get("/api/reading")
                        .param("metrics", "temperature")
                        .param("statistic", "AVERAGE")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-01-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }

    @Test
    void queryReading_onlyStartDate() throws Exception {
        mockMvc.perform(get("/api/reading")
                        .param("metrics", "temperature")
                        .param("statistic", "AVERAGE")
                        .param("startDate", "2023-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }

    @Test
    void queryReading_withoutDate() throws Exception {
        mockMvc.perform(get("/api/reading")
                        .param("metrics", "temperature")
                        .param("statistic", "AVERAGE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }
}