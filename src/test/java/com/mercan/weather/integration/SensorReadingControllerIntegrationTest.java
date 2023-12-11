package com.mercan.weather.integration;

import com.mercan.weather.entity.Reading;
import com.mercan.weather.repository.SensorReadingRepositoryImpl;
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
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class SensorReadingControllerIntegrationTest {

    @Autowired
    private SensorReadingRepositoryImpl sensorReadingRepositoryImpl;

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

    private Reading reading;

    @BeforeEach
    public void beforeEach() {
        reading = new Reading();
        // Set properties for sensorReading
        sensorReadingRepositoryImpl.save(reading);
    }

    @AfterEach
    public void afterEach() {
        sensorReadingRepositoryImpl.delete(reading);
    }

    @Test
    void querySensorReading_expect_success() throws Exception {
        this.mockMvc.perform(get("/api/reading")
                        .param("sensorIds", reading.getSensorId().toString())
                        .param("metrics", "temperature,humidity")
                        .param("statistic", "AVERAGE")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].sensorId").value(reading.getSensorId().toString()))
                .andExpect(jsonPath("$.result[0].statistics").isNotEmpty());
    }

    @Test
    void queryAllSensors_expect_success() throws Exception {
        this.mockMvc.perform(get("/api/reading")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }

    @Test
    void queryLatestData_expect_success() throws Exception {
        this.mockMvc.perform(get("/api/reading")
                        .param("metrics", "temperature,humidity")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }

    // Additional methods for update, delete, etc.

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}