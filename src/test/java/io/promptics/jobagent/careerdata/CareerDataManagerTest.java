package io.promptics.jobagent.careerdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.careerdata.model.CareerData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class CareerDataManagerTest {

    @Autowired
    private CareerDataManager careerDataManager;

    @Autowired
    private CareerDataRepository repository;
    private String id;

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");
    private ObjectMapper objectMapper;
    private CareerData careerDataPersisted;

    @BeforeEach
    void beforeEach() throws IOException {
        objectMapper = new ObjectMapper();
        careerDataPersisted = objectMapper.readValue(new ClassPathResource("career-data.json").getFile(), CareerData.class);
        CareerData saved = repository.save(careerDataPersisted);
        this.id = saved.getId();
    }

    @Test
    @DisplayName("get career data by id")
    void getCareerDataById() throws JsonProcessingException {
        String result = careerDataManager.run("Get data for id %s".formatted(id));
        CareerData careerDataFromDb = objectMapper.readValue(result, CareerData.class);
        assertThat(careerDataFromDb).usingRecursiveComparison().isEqualTo(careerDataPersisted);
    }

    @Test
    @DisplayName("modify email in basis")
    void modifyEmailInBasis() throws JsonProcessingException {
        Optional<CareerData> before = repository.findById(id);
        String result = careerDataManager.run("Change email from max.wurst@techgiant.com to max@wurst.com for id %s".formatted(id));
        System.out.println(result);
        assertThat(result).contains("Successfully updated document using set on basics.email");
        Optional<CareerData> after = repository.findById(id);
        assertThat(after.get().getBasics().getEmail()).isEqualTo("max@wurst.com");
    }
}