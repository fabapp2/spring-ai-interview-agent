package io.promptics.jobagent.careerdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.MongoDbConfig;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.careerdata.model.SectionWithId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest
@Import(CareerDataMongoEventListener.class)
@Testcontainers
class CareerDataRepositoryTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    private CareerData readCareerData() {
        try {
            File file = new ClassPathResource("career-data-complete.json").getFile();
            return objectMapper.readValue(file, CareerData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Container
    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:%s".formatted(MongoDbConfig.MONGODB_VERSION));

    @Autowired
    private CareerDataRepository repository;

    @Test
    @DisplayName("crud")
    void crud() {
        CareerData careerData = readCareerData();
        CareerData saved = repository.save(careerData);

        assertThat(careerData).usingRecursiveComparison().ignoringFields("id").isEqualTo(saved);

        assertThatIdsWereSet(careerData.getSkills());
        assertThatIdsWereSet(saved.getAwards());
        assertThatIdsWereSet(saved.getCertificates());
        assertThatIdsWereSet(saved.getEducation());
        assertThatIdsWereSet(saved.getInterests());
        assertThatIdsWereSet(saved.getLanguages());
        assertThatIdsWereSet(saved.getProjects());
        assertThatIdsWereSet(saved.getPublications());
        assertThatIdsWereSet(saved.getReferences());
        assertThatIdsWereSet(saved.getSkills());
        assertThatIdsWereSet(saved.getVolunteer());
        assertThatIdsWereSet(saved.getWork());
    }

    private void assertThatIdsWereSet(List<? extends SectionWithId> section) {
        section.stream()
                .forEach(s -> assertThat(s.getId()).isNotNull());
    }
}