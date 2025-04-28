package io.promptics.jobagent.careerdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.promptics.jobagent.MongoDbConfig;
import io.promptics.jobagent.careerdata.model.CareerData;
import io.promptics.jobagent.careerdata.model.SectionWithId;
import io.promptics.jobagent.careerdata.model.Work;
import org.junit.jupiter.api.*;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest
@Import({CareerDataRepository.class, CareerDataMongoEventListener.class, SectionIdProvider.class})
@Testcontainers
class CareerDataRepositoryTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private static String careerDataId;

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
    @Order(1)
    @DisplayName("crud")
    void crud() throws JsonProcessingException {
        CareerData careerData = readCareerData();
        CareerData saved = repository.save(careerData);
        careerDataId = saved.getId();

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

//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(saved));
    }
    
    @Test
    @Order(2)
    @DisplayName("update and read work section")
    void updateAndReadWorkSection() throws JsonProcessingException {

        List<Work> worksBefore = repository.readSection(careerDataId, Work.class);
        assertThat(worksBefore).hasSize(3);

        Work work = objectMapper.readValue("""
              {
                  "name": "SomeCompany",
                  "position": "Lead Developer",
                  "startDate": "2020-07",
                  "endDate": "2022-12",
                  "summary": "what a ride.",
                  "highlights": [
                    "Did a great job."
                  ]
              }
              """, Work.class);
        repository.addWork(careerDataId, work);
        List<Work> worksAfter = repository.readSection(careerDataId, Work.class);
        assertThat(worksAfter).hasSize(4);
        assertThatIdsWereSet(worksAfter);
    }

    private void assertThatIdsWereSet(List<? extends SectionWithId> section) {
        section.stream()
                .forEach(s -> assertThat(s.getId()).isNotNull());
    }
}