package com.mercan.weather.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercan.weather.entity.Sensor;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SensorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16.1")
                    .withDatabaseName("integration-tests-db")
                    .withUsername("testuser")
                    .withPassword("testpass");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void createSensor_success() throws Exception {
        Sensor sensor = new Sensor(/* initialize properties */);
        String sensorJson = new ObjectMapper().writeValueAsString(sensor);

        mockMvc.perform(post("/api/sensor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sensorJson))
                .andExpect(status().isCreated());
    }


    @Test
    void deleteSensor_success() throws Exception {
        UUID sensorId = UUID.randomUUID();

        mockMvc.perform(delete("/api/sensor/" + sensorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllSensors_success() throws Exception {
        mockMvc.perform(get("/api/sensor")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}