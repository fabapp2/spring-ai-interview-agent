package io.promptics.jobagent.careerdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.MongoDbConfig;
import io.promptics.jobagent.careerdata.model.CareerData;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CareerDataManagerTest {

    @Autowired
    private CareerDataManager careerDataManager;

    @Autowired
    private CareerDataRepository repository;
    private String id;

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));
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
        String result = careerDataManager.run("Get data", id);
        CareerData careerDataFromDb = objectMapper.readValue(result, CareerData.class);
        assertThat(careerDataFromDb).usingRecursiveComparison().isEqualTo(careerDataPersisted);
    }

    @Test
    @DisplayName("modify email in basis")
    void modifyEmailInBasis() {
        Optional<CareerData> before = repository.findById(id);
        assertThat(before.get().getBasics().getEmail()).isEqualTo("max.wurst@techgiant.com");
        String result = careerDataManager.run("Change email from max.wurst@techgiant.com to max@wurst.com", id);
        assertThat(result).contains("Successfully changed email from max.wurst@techgiant.com to max@wurst.com");
        Optional<CareerData> after = repository.findById(id);
        assertThat(after.get().getBasics().getEmail()).isEqualTo("max@wurst.com");
    }

    @Test
    @DisplayName("modify company name")
    void modifyCompanyName() {
        Optional<CareerData> before = repository.findById(id);
        assertThat(before.get().getWork().get(1).getName()).isEqualTo("MiniCorp");
        String result = careerDataManager.run("Change company name MiniCorp to MaxiCorp.", id);
        assertThat(result).contains("Successfully changed company name from MiniCorp to MaxiCorp");
        Optional<CareerData> after = repository.findById(id);
        assertThat(after.get().getWork().get(1).getName()).isEqualTo("MaxiCorp");
    }

    @Test
    @DisplayName("remove Python skill")
    void removeJavaSkill() {
        Optional<CareerData> before = repository.findById(id);
        assertThat(before.get().getSkills().get(0).getKeywords().get(1)).isEqualTo("Python");
        String result = careerDataManager.run("Remove Python from skills", id);
        assertThat(result).contains("Successfully removed Python from the set of skills");
        Optional<CareerData> after = repository.findById(id);
        assertThat(after.get().getSkills().get(0).getKeywords().get(1)).isEqualTo("C++");
        assertThat(after.get().getSkills().get(0).getKeywords()).doesNotContain("Python");
    }
}