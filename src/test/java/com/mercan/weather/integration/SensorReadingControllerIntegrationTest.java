package com.mercan.weather.integration;

import com.mercan.weather.entity.Reading;
import com.mercan.weather.repository.SensorReadingRepository;
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

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class SensorReadingControllerIntegrationTest {

    @Autowired
    private SensorReadingRepository sensorReadingRepository;

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

    private Reading sensorReading;

    @BeforeEach
    public void beforeEach() {
        sensorReading = sensorReadingRepository.save(createSensorReading());
    }

    @AfterEach
    public void afterEach() {
        sensorReadingRepository.delete(sensorReading);
    }

    // Test cases

    @Test
    void createSensorReading_expect_success() throws Exception {
        // Setup new sensor reading data
        // Perform mockMvc POST request
        // Expect successful creation status
    }

    @Test
    void querySensorReading_expect_success() throws Exception {
        // Perform mockMvc GET request with query parameters
        // Expect successful retrieval status
    }

    // Additional test cases for update, delete, not found scenarios, etc.

    // Helper methods for creating test data
}