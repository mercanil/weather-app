package com.mercan.weather.integration;

import com.mercan.weather.entity.Sensor;
import com.mercan.weather.repository.SensorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class SensorControllerIntegrationTest {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("integration-tests-db")
                    .withUsername("username")
                    .withPassword("password");

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    private Sensor sensor;

    @BeforeEach
    public void beforeEach() {
        sensor = new Sensor();
        // Set properties for sensor
        sensor = sensorRepository.save(sensor);
    }

    @AfterEach
    public void afterEach() {
        sensorRepository.delete(sensor);
    }

    @Test
    void createSensor_expect_success() throws Exception {
        Sensor newSensor = new Sensor();
        // Set properties for newSensor

        this.mockMvc.perform(post("/api/sensor")
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(newSensor)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllSensors_expect_success() throws Exception {
        this.mockMvc.perform(get("/api/sensor")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getSensorById_expect_success() throws Exception {
        this.mockMvc.perform(get("/api/sensor/" + sensor.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateSensor_expect_success() throws Exception {
        sensor.setName("updatedValue");
        this.mockMvc.perform(put("/api/sensor/" + sensor.getId())
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(sensor)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSensor_expect_success() throws Exception {
        this.mockMvc.perform(delete("/api/sensor/" + sensor.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}