package io.promptics.jobagent.careerdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.model.CareerData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
class CareerDataRepositoryTest {

    @Autowired
    private ObjectMapper objectMapper;

    private CareerData readCareerData() {
        try {
            File file = new ClassPathResource("career-data.json").getFile();
            return objectMapper.readValue(file, CareerData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Autowired
    private CareerDataRepository repository;

    @Test
    @DisplayName("crud")
    void crud() {
        CareerData careerData = readCareerData();
        CareerData saved = repository.save(careerData);
        assertThat(careerData).usingRecursiveComparison().isEqualTo(saved);
    }
}