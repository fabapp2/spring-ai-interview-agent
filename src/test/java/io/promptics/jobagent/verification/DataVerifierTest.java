package io.promptics.jobagent.verification;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.MongoDbConfig;
import io.promptics.jobagent.careerdata.model.CareerData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class DataVerifierTest {

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));

    @Autowired
    MongoTemplate mongoTemplate;
    private String id;

    @Autowired
    private DataVerifier dataVerifier;

    @BeforeEach
    void beforeEach() {
        try {
            CareerData careerData = new ObjectMapper().readValue(new ClassPathResource("career-data.json").getFile(), CareerData.class);
            CareerData saved = mongoTemplate.save(careerData);
            id = saved.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("should render career data")
    void shouldRenderCareerData() {
        String response = dataVerifier.execute(null, id);
        assertThat(response).contains("TechGiant");
    }
    
    @Test
    @DisplayName("should change company name")
    void shouldChangeCompanyName() {
        String userInput = "Please change the company name 'TechGiant' to 'TechDwarf'.";
        String response = dataVerifier.execute(userInput, id);
        CareerData careerData = mongoTemplate.findById(id, CareerData.class);
        assertThat(response).contains("Successfully changed").contains("TechGiant to TechDwarf");
        assertThat(careerData.getWork().get(2).getName()).isEqualTo("TechDwarf");
    }

}